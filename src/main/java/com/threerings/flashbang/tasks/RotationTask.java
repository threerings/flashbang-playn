//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import tripleplay.util.Interpolator;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.components.RotationComponent;

public class RotationTask extends InterpolatingTask
{
    public RotationTask (float rotation, float time, Interpolator interp)
    {
        super(time, interp);
        _toRotation = rotation;
    }

    public RotationTask (float rotation, float time)
    {
        this(rotation, time, Interpolator.LINEAR);
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof RotationComponent,
            "RotationTask only operates on GameObjects that implement RotationComponent");

        RotationComponent ac = (RotationComponent) obj;

        if (_elapsedTime == 0) {
            _fromRotation = ac.rotation();
        }

        _elapsedTime += dt;

        ac.setRotation(interpolate(_fromRotation, _toRotation));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public RotationTask clone ()
    {
        return new RotationTask(_toRotation, _totalTime, _interp);
    }

    protected final float _toRotation;
    protected float _fromRotation;
}
