//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Layer;

import react.Value;
import react.ValueView;

import flashbang.anim.rsrc.Animatable;
import flashbang.anim.rsrc.KeyframeConf;
import flashbang.anim.rsrc.KeyframeType;
import flashbang.anim.rsrc.AnimConf;

public class AnimationController
{
    public AnimationController (float framerate, Collection<Animatable> animation) {
        _framerate = framerate;

        _layers = Lists.newArrayListWithCapacity(animation.size());
        int frames = 0;
        for (Animatable anim : animation) {
            _layers.add(new LayerState(anim));
            frames = Math.max(frames, anim.animation.frames());
        }
        _frames = frames;

        // Force-update to frame 0 of the animation
        draw(0);
    }

    public int frames () { return _frames; }

    public ValueView<Integer> frameChanged () { return _frame; }

    public int frame () { return _frame.get(); }

    public void setFrame (int frame) {
        if (_frame.get() == frame) return;
        draw(frame);
        _frame.update(frame);
    }

    public boolean stopped () { return _stopped; }

    public void setStopped (boolean stopped) { _stopped = stopped; }

    public void update (float dt) {
        if (_stopped) return;

        _playTime += dt;

        setFrame((int)(_playTime * _framerate) % frames());
    }

    /**
     * Draws the given frame. This doesn't change the frame the animation is on.
     */
    public void draw (int frame) {
        if (frame < _frame.get()) {
            for (LayerState state : _layers) {
                state.reset();
            }
        }

        float x = 0, y = 0, xScale = 1, yScale = 1, rotation = 0, alpha = 1;
        // Update our layers
        for (LayerState state : _layers) {
            Layer layer = state.layer;

            for (KeyframeType kt : KeyframeType.values()) {
                // Interpolate between this keyframe and the next
                float interped = state.find(kt, frame).interp(frame);
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

    protected final List<LayerState> _layers;
    protected final float _framerate;
    protected final int _frames;
    protected final Value<Integer> _frame = Value.create(0);

    protected boolean _stopped;
    protected float _playTime;

    protected static class LayerState {
        public final AnimConf desc;
        public final Layer layer;
        public final KeyframeConf[] prev = new KeyframeConf[KeyframeType.values().length];

        public LayerState (Animatable anim) {
            desc = anim.animation;
            layer = anim.layer;
            reset();
        }

        public void reset () {
            for (KeyframeType kt : KeyframeType.values()) {
                prev[kt.ordinal()] = desc.keyframes().get(kt);
            }
        }

        public KeyframeConf find (KeyframeType kt, int frame) {
            prev[kt.ordinal()] = prev[kt.ordinal()].find(frame);
            return prev[kt.ordinal()];
        }
    }
}
