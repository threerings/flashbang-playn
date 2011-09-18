//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.desc;

import playn.core.Json;

import tripleplay.util.JsonUtil;

public abstract class BasicNamedDataDesc
    implements NamedDataDesc
{
    public String name;

    public String getName ()
    {
        return name;
    }

    public BasicNamedDataDesc ()
    {
    }

    public BasicNamedDataDesc (String name)
    {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see com.threerings.who.datadesc.INamedDataDesc#getId()
     */
    public final int getId ()
    {
        return DataDescUtil.nameToId(name);
    }

    @Override
    public String toString ()
    {
        return "" + getClass().getSimpleName() + " [id=" + getId() + ", name=" + name + "]";
    }

    @Override
    public int hashCode ()
    {
        return getId();
    }

    @Override
    public boolean equals (Object o)
    {
        if (!(o instanceof BasicNamedDataDesc)) {
            return false;
        }

        // Note: we only exist within a DataMgr, and would fail-fast on hash collision
        return o.getClass() == getClass() && o.hashCode() == hashCode();
    }

    public void fromJson (Json.Object json)
    {
        name = JsonUtil.requireString(json, "name");
    }
}
