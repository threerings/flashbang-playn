//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import playn.core.Layer;

import flashbang.anim.rsrc.Keyframe;
import flashbang.anim.rsrc.KeyframeType;
import flashbang.anim.rsrc.LayerAnimation;
import flashbang.anim.rsrc.ModelAnimation;

public class AnimationController
{
    public AnimationController (Model model, ModelAnimation anim)
    {
        _anim = anim;
        _model = model;

        // Build our LayerAnimData structure
        for (LayerAnimation layerAnim : anim.layers()) {
            Layer layer = model.getLayer(layerAnim.layerSelector());
            Preconditions.checkNotNull(layer, "Invalid layer [selector=%s]",
                layerAnim.layerSelector());
            _layers.add(new LayerState(layerAnim, layer));
        }

        // Force-update to frame 0 of the animation
        _frame = -1;
        setFrameInternal(0);
    }

    public int frames ()
    {
        return _anim.frames();
    }

    public int frame () { return _frame; }

    public void setFrame (int frame)
    {
        setFrameInternal(frame);
        _elapsedTime = (_frame / _model.framerate());
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
        if (_stopped) { return; }

        // Calculate our current frame
        _elapsedTime += dt;
        int newFrame = (int) (_model.framerate() * _elapsedTime);

        // Apply end behavior
        if (newFrame >= frames()) {
            newFrame %= frames();
        }

        setFrameInternal(newFrame);
    }

    protected void resetKeyframes () {
    }

    protected void setFrameInternal (int frame)
    {
        if (_frame == frame) { return; }

        Preconditions.checkArgument(frame >= 0 && frame < frames(),
            "Frame out of bounds [frame=%s, totalFrames=%s]", frame, frames());

        if (frame < _frame) {
            for (LayerState state : _layers) {
                state.reset();
            }
        }
        _frame = frame;

        float x = 0, y = 0, xScale = 1, yScale = 1, rotation = 0, alpha = 1;
        // Update our layers
        for (LayerState state : _layers) {
            Layer layer = state.layer;

            for (KeyframeType kt : KeyframeType.values()) {
                // Interpolate between this keyframe and the next
                float interped = state.find(kt, _frame).interp(frame);
                switch (kt) {
                case X_LOCATION: x = interped; break;
                case Y_LOCATION: y = interped; break;
                case X_SCALE:    xScale = interped; break;
                case Y_SCALE:    yScale = interped; break;
                case ROTATION:   rotation = interped; break;
                case ALPHA:      alpha = interped; break;
                }
            }

            layer.setTranslation(x, y);
            layer.setScale(xScale, yScale);
            layer.setRotation(rotation);
            layer.setAlpha(alpha);
        }
    }

    protected final ModelAnimation _anim;
    protected final Model _model;
    protected final List<LayerState> _layers = Lists.newArrayList();

    protected int _frame;
    protected boolean _stopped;
    protected float _elapsedTime;

    protected static class LayerState
    {
        public final LayerAnimation desc;
        public final Layer layer;
        public final Keyframe[] prev = new Keyframe[KeyframeType.values().length];

        public LayerState (LayerAnimation desc, Layer layer) {
            this.desc = desc;
            this.layer = layer;
            reset();
        }

        public void reset () {
            for (KeyframeType kt : KeyframeType.values()) {
                prev[kt.ordinal()] = desc.keyframes().get(kt);
            }
        }

        public Keyframe find (KeyframeType kt, int frame) {
            prev[kt.ordinal()] = prev[kt.ordinal()].find(frame);
            return prev[kt.ordinal()];
        }
    }
}
