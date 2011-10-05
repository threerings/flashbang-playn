//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import playn.core.GroupLayer;
import playn.core.Layer;

import react.Value;
import react.ValueView;

import flashbang.SceneObject;
import flashbang.anim.rsrc.AnimConf;
import flashbang.anim.rsrc.Animatable;
import flashbang.anim.rsrc.KeyframeConf;
import flashbang.anim.rsrc.KeyframeType;
import flashbang.anim.rsrc.MovieConf;

public class Movie extends SceneObject
{
    public Movie (Layer root, Map<String, GroupLayer> exports,
        Multimap<String, Animatable> animations) {
        _root = root;
        _animations = animations;
        _exports = exports;
        play(MovieConf.DEFAULT_ANIMATION);
    }

    /** Returns the number of frames in the currently playing animation. */
    public int frames () { return _frames; }

    public ValueView<Integer> frameChanged () { return _frame; }

    public int frame () { return _frame.get(); }

    public void setFrame (int frame) {
        if (_frame.get() == frame) return;
        draw(frame);
        _frame.update(frame);
    }

    public GroupLayer getLayer (String name) { return _exports.get(name); }

    public boolean stopped () { return _stopped; }

    public void setStopped (boolean stopped) { _stopped = stopped; }

    public float framerate () { return 30; }

    @Override public Layer layer () { return _root; }

    public void play (String name) {
        _stopped = false;
        _layers.clear();
        _playTime = 0;
        _frames = 0;

        for (Animatable anim : _animations.get(name)) {
            _layers.add(new LayerState(anim));
            _frames = Math.max(_frames, anim.animation.frames());
        }

        draw(0); // Force-update to frame 0 of the animation
        _frame.update(0);
    }

    @Override protected void update (float dt) {
        super.update(dt);
        if (_stopped) return;

        _playTime += dt;

        setFrame((int)(_playTime * framerate()) % _frames);
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
            Layer layer = state.layer;
            layer.setTranslation(x, y);
            layer.setScale(xScale, yScale);
            layer.setRotation(rotation);
            layer.setAlpha(alpha);
        }
    }

    protected int _frames;
    protected boolean _stopped;
    protected float _playTime;

    protected final List<LayerState> _layers = Lists.newArrayList();
    protected final Value<Integer> _frame = Value.create(0);
    protected final Multimap<String, Animatable> _animations;
    protected final Map<String, GroupLayer> _exports;
    protected final Layer _root;

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
