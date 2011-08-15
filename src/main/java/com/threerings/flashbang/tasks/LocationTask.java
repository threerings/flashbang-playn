//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.components.LocationComponent;

import tripleplay.util.Interpolator;

public class LocationTask extends InterpolatingTask
{
    public LocationTask (float x, float y, float time, Interpolator interp)
    {
        super(time, interp);
        _toX = x;
        _toY = y;
    }

    public LocationTask (float x, float y, float time)
    {
        this(x, y, time, Interpolator.LINEAR);
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof LocationComponent,
            "LocationTask only operates on GameObjects that implement LocationComponent");

        LocationComponent lc = (LocationComponent) obj;

        if (_elapsedTime == 0) {
            _fromX = lc.x();
            _fromY = lc.y();
        }

        _elapsedTime += dt;

        lc.setX(interpolate(_fromX, _toX));
        lc.setY(interpolate(_fromY, _toY));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public LocationTask clone ()
    {
        return new LocationTask(_toX, _toY, _totalTime, _interp);
    }

    protected final float _toX;
    protected final float _toY;

    protected float _fromX;
    protected float _fromY;
}
