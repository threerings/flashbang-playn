//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import java.util.Map;

import com.google.common.base.Preconditions;

public final class DescRef <T extends NamedDataDesc>
{
    public String name;

    public static DataMgr<?> requireMatchingMgr (Class<?> descType, Map<Class<?>, DataMgr<?>> mgrs)
    {
        DataMgr<?> mgr = mgrs.get(descType);
        Preconditions.checkState(mgr != null,
            "Couldn't find DataMgr for DescRef type [descType=%s]", descType);
        return mgr;
    }

    /**
     * Checks if an iterable containing descriptor references has one that refers to a specific
     * element. This is a linear search and assumes references will not contain null.
     */
    public static <T extends NamedDataDesc> boolean contains (Iterable<DescRef<T>> elems, T elem)
    {
        for (DescRef<T> ref : elems) {
            if (ref.get().equals(elem)) {
                return true;
            }
        }
        return false;
    }

    public DescRef ()
    {
    }

    public DescRef (String name)
    {
        this.name = name;
    }

    public T get ()
    {
        return _desc;
    }

    public void resolve (DataMgr<?> mgr)
    {
        Preconditions.checkState(_desc == null, "resolve() has already been called");
        @SuppressWarnings("unchecked")
        T desc = (T) mgr.requireData(name);
        _desc = desc;
    }

    @Override
    public String toString ()
    {
        return "DescRef [name=" + name + ", resolved=" + (_desc != null) + "]";
    }

    protected T _desc;
}
