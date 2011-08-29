//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.threerings.flashbang.util.Loadable;

public abstract class Resource extends Loadable
{
    /** The unique name for this Resource */
    public final String name;

    public Resource (String name)
    {
        this.name = name;
    }

    public String group ()
    {
        return _group;
    }

    /** The group that this Resource belongs to. Set by ResourceGroup. */
    String _group;
}
