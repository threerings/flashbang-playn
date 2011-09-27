//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import flashbang.GameObject;
import flashbang.ObjectTask;

public abstract class TimedTask extends ObjectTask
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

    protected final float _totalTime;
    protected float _elapsedTime;
}
