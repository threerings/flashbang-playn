//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.rsrc;

import flashbang.util.Loadable;

public abstract class Resource extends Loadable
{
    /** The unique name for this Resource */
    public final String name;

    public Resource (String name) {
        this.name = name;
    }

    /** The group that this Resource belongs to. Set by ResourceGroup. */
    public String group () { return _group; }

    String _group;
}
