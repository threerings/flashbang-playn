//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import tripleplay.util.Interpolator;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
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
    public void init (GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof RotationComponent,
            "RotationTask only operates on GameObjects that implement RotationComponent");
        _target = (RotationComponent)obj;
    }

    @Override
    public boolean update (float dt)
    {
        if (_elapsedTime == 0) {
            _fromRotation = _target.rotation();
        }

        _elapsedTime += dt;

        _target.setRotation(interpolate(_fromRotation, _toRotation));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public ObjectTask createClone ()
    {
        return new RotationTask(_toRotation, _totalTime, _interp);
    }

    protected RotationComponent _target;

    protected final float _toRotation;
    protected float _fromRotation;
}
