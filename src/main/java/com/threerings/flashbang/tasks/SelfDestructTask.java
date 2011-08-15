//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public class SelfDestructTask extends ObjectTask
{
    @Override
    public void init (GameObject obj)
    {
        _obj = obj;
    }

    @Override
    public boolean update (float dt)
    {
        _obj.destroySelf();
        return true;
    }

    @Override
    protected ObjectTask createClone ()
    {
        return new SelfDestructTask();
    }

    protected GameObject _obj;
}
