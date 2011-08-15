//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class FunctionTask extends ObjectTask
{
    public FunctionTask (Runnable runnable)
    {
        _runnable = runnable;
    }

    @Override
    public void init (GameObject obj)
    {
        // nada
    }

    @Override
    public boolean update (float dt)
    {
        _runnable.run();
        return true;
    }

    @Override
    public ObjectTask clone ()
    {
        return new FunctionTask(_runnable);
    }

    protected final Runnable _runnable;
}
