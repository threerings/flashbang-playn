//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import tripleplay.util.Randoms;

import com.threerings.flashbang.ObjectTask;

public class VariableTimedTask extends TimedTask
{
    /**
     * Creates a new VariableTimedTask, which will complete some time between
     * <code>minTime</code> (inclusive) and <code>maxTime</code> (exclusive).
     */
    public VariableTimedTask (float minTime, float maxTime, Randoms rands)
    {
        super(rands.getInRange(minTime, maxTime));
        _minTime = minTime;
        _maxTime = maxTime;
        _rands = rands;
    }

    @Override
    public ObjectTask clone ()
    {
        return new VariableTimedTask(_minTime, _maxTime, _rands);
    }

    protected final float _minTime;
    protected final float _maxTime;
    protected final Randoms _rands;
}
