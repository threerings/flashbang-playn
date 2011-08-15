//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class FunctionTask
    implements ObjectTask
{
    public FunctionTask (Runnable runnable)
    {
        _runnable = runnable;
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        _runnable.run();
        return true;
    }

    @Override
    public FunctionTask clone ()
    {
        return new FunctionTask(_runnable);
    }

    protected final Runnable _runnable;
}
