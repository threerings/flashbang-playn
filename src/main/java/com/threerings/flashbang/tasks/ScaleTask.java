//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import tripleplay.util.Interpolator;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.components.ScaleComponent;

public class ScaleTask extends InterpolatingTask
{
    public ScaleTask (float x, float y, float time, Interpolator interp)
    {
        super(time, interp);
        _toX = x;
        _toY = y;
    }

    public ScaleTask (float x, float y, float time)
    {
        this(x, y, time, Interpolator.LINEAR);
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof ScaleComponent,
            "ScaleTask only operates on GameObjects that implement ScaleComponent");

        ScaleComponent sc = (ScaleComponent) obj;

        if (_elapsedTime == 0) {
            _fromX = sc.scaleX();
            _fromY = sc.scaleY();
        }

        _elapsedTime += dt;

        sc.setScaleX(interpolate(_fromX, _toX));
        sc.setScaleY(interpolate(_fromY, _toY));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public ScaleTask clone ()
    {
        return new ScaleTask(_toX, _toY, _totalTime, _interp);
    }

    protected final float _toX;
    protected final float _toY;

    protected float _fromX;
    protected float _fromY;
}
