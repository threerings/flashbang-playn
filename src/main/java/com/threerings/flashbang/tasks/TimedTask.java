//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class TimedTask extends ObjectTask
{
    public TimedTask (float time)
    {
        _totalTime = time;
    }

    @Override
    public void init (GameObject obj)
    {
        // nada
    }

    @Override
    public boolean update (float dt)
    {
        _elapsedTime += dt;
        return (_elapsedTime >= _totalTime);
    }

    @Override
    protected ObjectTask createClone ()
    {
        return new TimedTask(_totalTime);
    }

    @Override
    protected void initClone (ObjectTask task)
    {
        ((TimedTask) task)._elapsedTime = 0;
    }

    protected final float _totalTime;
    protected float _elapsedTime;
}
