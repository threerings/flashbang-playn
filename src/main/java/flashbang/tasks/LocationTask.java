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
import flashbang.components.LocationComponent;

public class LocationTask extends InterpolatingTask
{
    public LocationTask (Layer layer, float x, float y, float time, Interpolator interp)
    {
        super(time, interp);
        _layer = layer;
        _toX = x;
        _toY = y;
    }

    public LocationTask (Layer layer, float x, float y, float time)
    {
        this(layer, x, y, time, Interpolator.LINEAR);
    }

    public LocationTask (float x, float y, float time, Interpolator interp)
    {
        this(null, x, y, time, interp);
    }

    public LocationTask (float x, float y, float time)
    {
        this(null, x, y, time, Interpolator.LINEAR);
    }

    @Override
    public void init (GameObject obj)
    {
        if (_layer != null) {
            _target = new LayerWrapper(_layer);
        } else {
            Preconditions.checkArgument(obj instanceof LocationComponent,
                "LocationTask only operates on GameObjects that implement LocationComponent");
            _target = (LocationComponent) obj;
        }
    }

    @Override
    public boolean update (float dt)
    {
        if (_elapsedTime == 0) {
            _fromX = _target.x();
            _fromY = _target.y();
        }

        _elapsedTime += dt;

        _target.setX(interpolate(_fromX, _toX));
        _target.setY(interpolate(_fromY, _toY));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public ObjectTask clone ()
    {
        return new LocationTask(_layer, _toX, _toY, _totalTime, _interp);
    }

    protected final Layer _layer;
    protected final float _toX;
    protected final float _toY;

    protected LocationComponent _target;
    protected float _fromX;
    protected float _fromY;
}
