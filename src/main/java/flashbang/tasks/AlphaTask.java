//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import playn.core.Layer;

import com.google.common.base.Preconditions;

import tripleplay.util.Interpolator;

import flashbang.GameObject;
import flashbang.ObjectTask;
import flashbang.components.AlphaComponent;

public class AlphaTask extends InterpolatingTask
{
    public AlphaTask (Layer layer, float alpha, float time, Interpolator interp)
    {
        super(time, interp);
        _layer = layer;
        _toAlpha = alpha;
    }

    public AlphaTask (Layer layer, float alpha, float time)
    {
        this(layer, alpha, time, Interpolator.LINEAR);
    }

    public AlphaTask (float alpha, float time, Interpolator interp)
    {
        this(null, alpha, time, interp);
    }

    public AlphaTask (float alpha, float time)
    {
        this(null, alpha, time, Interpolator.LINEAR);
    }

    @Override public void init (GameObject obj)
    {
        if (_layer != null) {
            _target = new LayerWrapper(_layer);
        } else {
            Preconditions.checkArgument(obj instanceof AlphaComponent,
                "AlphaTask only operates on GameObjects that implement AlphaComponent");
            _target = (AlphaComponent) obj;
        }
    }

    @Override public boolean update (float dt)
    {
        if (_elapsedTime == 0) {
            _fromAlpha = _target.alpha();
        }

        _elapsedTime += dt;

        _target.setAlpha(interpolate(_fromAlpha, _toAlpha));

        return (_elapsedTime >= _totalTime);
    }

    @Override public ObjectTask clone ()
    {
        return new AlphaTask(_layer, _toAlpha, _totalTime, _interp);
    }

    protected final Layer _layer;
    protected final float _toAlpha;

    protected AlphaComponent _target;
    protected float _fromAlpha;
}
