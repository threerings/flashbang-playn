//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

public class GameObjectRef
{
    public static GameObjectRef Null ()
    {
        return NULL;
    }

    public void destroyObject ()
    {
        if (isLive()) {
            _obj.destroySelf();
        }
    }

    public GameObject obj ()
    {
        return _obj;
    }

    public <T> T obj (Class<T> clazz)
    {
        return clazz.cast(obj());
    }

    public boolean isLive ()
    {
        return (_obj != null);
    }

    public boolean isNull ()
    {
        return (_obj == null);
    }

    GameObject _obj;
    GameObjectRef _next;
    GameObjectRef _prev;

    protected static final GameObjectRef NULL = new GameObjectRef();
}
