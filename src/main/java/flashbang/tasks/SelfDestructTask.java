//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import flashbang.GameObject;
import flashbang.ObjectTask;

public class SelfDestructTask extends ObjectTask
{
    @Override public void init (GameObject obj)
    {
        _obj = obj;
    }

    @Override public boolean update (float dt)
    {
        _obj.destroySelf();
        return true;
    }

    @Override public ObjectTask clone ()
    {
        return new SelfDestructTask();
    }

    protected GameObject _obj;
}
