//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim;

import java.util.List;

import playn.core.Layer;
import tripleplay.util.Interpolator;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.threerings.flashbang.anim.rsrc.KeyframeDesc;
import com.threerings.flashbang.anim.rsrc.LayerAnimDesc;
import com.threerings.flashbang.anim.rsrc.ModelAnimDesc;

public class AnimationController
{
    public AnimationController (Model model, ModelAnimDesc anim)
    {
        _desc = anim;
        _model = model;

        // Build our LayerAnimData structure
        _layerData = Lists.newArrayList();
        for (LayerAnimDesc layerAnimDesc : anim.layerAnims) {
            Layer layer = model.getLayer(layerAnimDesc.layerSelector);
            Preconditions.checkState(layer != null, "Invalid layer [selector=%s]",
                layerAnimDesc.layerSelector);
            _layerData.add(new LayerAnimData(layerAnimDesc, layer));
        }
    }

    public int totalFrames ()
    {
        return _desc.totalFrames();
    }

    public int curFrame ()
    {
        return _curFrame;
    }

    public void setCurFrame (int curFrame)
    {
        setCurFrameInternal(curFrame);
        _elapsedTime = (_curFrame / _desc.framerate);
    }

    public boolean stopped ()
    {
        return _stopped;
    }

    public void setStopped (boolean stopped)
    {
        _stopped = stopped;
    }

    public void update (float dt)
    {
        if (_stopped) {
            return;
        }

        // Calculate our current frame
        _elapsedTime += dt;
        int newFrame = (int) (_desc.framerate * _elapsedTime);

        // Apply end behavior
        if (newFrame >= _desc.totalFrames()) {
            switch (_desc.endBehavior) {
            case STOP:
                newFrame = _desc.totalFrames() - 1;
                break;

            case LOOP:
                newFrame %= _desc.totalFrames();
                break;
            }
        }

        setCurFrameInternal(newFrame);
    }

    protected void setCurFrameInternal (int frame)
    {
        if (_curFrame == frame) {
            return;
        }

        Preconditions.checkArgument(frame >= 0 && frame < _desc.totalFrames(),
            "Frame out of bounds [frame=%s, totalFrames=%s]", frame, _desc.totalFrames());

        _curFrame = frame;

        // Update our layers
        for (LayerAnimData layerData : _layerData) {
            KeyframeDesc kf = layerData.getKeyframe(_curFrame);
            Layer layer = layerData.layer;

            // Interpolate between this keyframe and the next
            if (kf.next() != null) {
                KeyframeDesc next = kf.next();
                Interpolator interp = kf.interp;
                int totalFrames = kf.endFrameIdx() - kf.frameIdx + 1;
                float totalTime = _desc.framerate * totalFrames;
                int elapsedFrames = _curFrame - kf.frameIdx;
                float elapsedTime = _desc.framerate * elapsedFrames;

                layer.setTranslation(
                    interp.apply(kf.x, next.x - kf.x, elapsedTime, totalTime),
                    interp.apply(kf.y, next.y - kf.y, elapsedTime, totalTime));
                layer.setScale(
                    interp.apply(kf.scaleX, next.scaleX - kf.scaleX, elapsedTime, totalTime),
                    interp.apply(kf.scaleY, next.scaleY - kf.scaleY, elapsedTime, totalTime));
                layer.setRotation(
                    interp.apply(kf.rotation, next.rotation - kf.rotation, elapsedTime, totalTime));
                layer.setAlpha(
                    interp.apply(kf.alpha, next.alpha - kf.alpha, elapsedTime, totalTime));

            } else {
                layer.setTranslation(kf.x, kf.y);
                layer.setScale(kf.scaleX, kf.scaleY);
                layer.setRotation(kf.rotation);
                layer.setAlpha(kf.alpha);
            }

            // Don't interpolate discrete values
            layer.setVisible(kf.visible);
        }
    }

    protected final ModelAnimDesc _desc;
    protected final Model _model;
    protected final List<LayerAnimData> _layerData;

    protected int _curFrame;
    protected boolean _stopped;
    protected float _elapsedTime;

    protected static class LayerAnimData
    {
        public final LayerAnimDesc desc;
        public final Layer layer;

        public KeyframeDesc getKeyframe (int frameIdx) {
            // Invalidate our cached keyframe?
            if (_cachedKeyframe != null && !_cachedKeyframe.validForFrame(frameIdx)) {
                // Can we save ourselves from looking up our keyframe again?
                if (_cachedKeyframe.next() != null &&
                        _cachedKeyframe.next().validForFrame(frameIdx)) {
                    _cachedKeyframe = _cachedKeyframe.next();
                } else {
                    _cachedKeyframe = null;
                }
            }

            if (_cachedKeyframe == null) {
                _cachedKeyframe = desc.getKeyframe(frameIdx);
            }
            return _cachedKeyframe;
        }

        public LayerAnimData (LayerAnimDesc desc, Layer layer) {
            this.desc = desc;
            this.layer = layer;
        }

        protected KeyframeDesc _cachedKeyframe;
    }
}
