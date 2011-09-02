//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import javax.annotation.Nullable;

public final class GameObjectRef
{
    /**
     * @return the GameObjectRef for the given GameObject, or a null ref if the object is null.
     */
    public static GameObjectRef get (@Nullable GameObject obj)
    {
        return (obj != null ? obj.ref() : NULL);
    }

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

    public boolean isLive ()
    {
        return (_obj != null);
    }

    public boolean isNull ()
    {
        return (_obj == null);
    }

    GameObjectRef ()
    {
        // disallow construction
    }

    GameObject _obj;
    GameObjectRef _next;
    GameObjectRef _prev;

    protected static final GameObjectRef NULL = new GameObjectRef();
}
