//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class TimedTask
    implements ObjectTask
{
    public TimedTask (float time)
    {
        _totalTime = time;
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        _elapsedTime += dt;
        return (_elapsedTime >= _totalTime);
    }

    @Override
    public TimedTask clone ()
    {
        TimedTask theClone = null;
        try {
            theClone = (TimedTask) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        theClone._elapsedTime = 0;
        return theClone;
    }

    protected final float _totalTime;
    protected float _elapsedTime;
}
