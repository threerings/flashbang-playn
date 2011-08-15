//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import tripleplay.util.Interpolator;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.components.AlphaComponent;

public class AlphaTask extends InterpolatingTask
{
    public AlphaTask (float alpha, float time, Interpolator interp)
    {
        super(time, interp);
        _toAlpha = alpha;
    }

    public AlphaTask (float alpha, float time)
    {
        this(alpha, time, Interpolator.LINEAR);
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof AlphaComponent,
            "AlphaTask only operates on GameObjects that implement AlphaComponent");

        AlphaComponent ac = (AlphaComponent) obj;

        if (_elapsedTime == 0) {
            _fromAlpha = ac.alpha();
        }

        _elapsedTime += dt;

        ac.setAlpha(interpolate(_fromAlpha, _toAlpha));

        return (_elapsedTime >= _totalTime);
    }

    @Override
    public AlphaTask clone ()
    {
        return new AlphaTask(_toAlpha, _totalTime, _interp);
    }

    protected final float _toAlpha;
    protected float _fromAlpha;
}
