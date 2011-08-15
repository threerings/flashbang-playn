//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.ObjectTask;

public class ParallelTask extends TaskContainer
{
    public ParallelTask (ObjectTask ...subtasks)
    {
        super(Type.PARALLEL, subtasks);
    }

    @Override
    protected TaskContainer createClone ()
    {
        return new ParallelTask();
    }
}
