//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.threerings.flashbang.util.Loadable;

public abstract class Resource<T> extends Loadable
{
    public final String path;

    public Resource (String path)
    {
        this.path = path;
    }

    public abstract T get ();
}
