//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.ResourceCallback;

public abstract class Resource<T>
{
    public final String path;

    public Resource (String path)
    {
        this.path = path;
    }

    public abstract void load (ResourceCallback<? super T> callback);
    public abstract T get ();
}
