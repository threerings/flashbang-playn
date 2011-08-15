//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class SelfDestructTask
    implements ObjectTask
{
    @Override
    public boolean update (float dt, GameObject obj)
    {
        obj.destroySelf();
        return true;
    }

    @Override
    public SelfDestructTask clone ()
    {
        return new SelfDestructTask();
    }
}
