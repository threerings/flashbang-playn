//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import flashbang.GameObject;
import flashbang.ObjectTask;

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
