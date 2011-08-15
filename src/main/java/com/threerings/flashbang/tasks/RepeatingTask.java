//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.ObjectTask;

public class RepeatingTask extends TaskContainer
{
    public RepeatingTask (ObjectTask ...subtasks)
    {
        super(Type.REPEATING, subtasks);
    }

}
