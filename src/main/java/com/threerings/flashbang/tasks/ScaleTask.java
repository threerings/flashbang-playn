//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import tripleplay.util.Interpolator;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
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
    public void init (GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof ScaleComponent,
            "ScaleTask only operates on GameObjects that implement ScaleComponent");
        _target = (ScaleComponent)obj;
    }

    @Override
    public boolean update (float dt)
    {
        if (_elapsedTime == 0) {
            _fromX = _target.scaleX();
            _fromY = _target.scaleY();
        }

        _elapsedTime += dt;

        _target.setScaleX(interpolate(_fromX, _toX));
        _target.setScaleY(interpolate(_fromY, _toY));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public ObjectTask clone ()
    {
        return new ScaleTask(_toX, _toY, _totalTime, _interp);
    }

    protected ScaleComponent _target;

    protected final float _toX;
    protected final float _toY;

    protected float _fromX;
    protected float _fromY;
}
