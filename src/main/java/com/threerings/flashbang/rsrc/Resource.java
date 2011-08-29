//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.threerings.flashbang.util.Loadable;

public abstract class Resource<T> extends Loadable
{
    /** The unique path for this Resource */
    public final String path;

    public Resource (String path)
    {
        this.path = path;
    }

    public String group ()
    {
        return _group;
    }

    public abstract T get ();

    /** The group that this Resource belongs to */
    String _group;
}
