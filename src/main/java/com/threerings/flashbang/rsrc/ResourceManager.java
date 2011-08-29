//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.threerings.flashbang.util.Loadable;

public class ResourceManager
{
    public Resource getResource (String name)
    {
        return _resources.get(name);
    }

    public Resource requireResource (String name)
    {
        Resource rsrc = _resources.get(name);
        Preconditions.checkNotNull(rsrc, "No such resource [name=%s]", name);
        return rsrc;
    }

    public boolean isLoaded (String name)
    {
        return (getResource(name) != null);
    }

    /**
     * Unloads all Resources that belong to the specified group
     */
    public void unload (String group)
    {
        for (Resource rsrc : Lists.newArrayList(_resources.values())) {
            if (rsrc.group().equals(group)) {
                _resources.remove(rsrc.name);
            }
        }
    }

    /**
     * Unloads all Resources
     */
    public void unloadAll ()
    {
        _resources.clear();
    }

    public void registerFactory (String type, ResourceFactory factory)
    {
        _factories.put(type, factory);
    }

    public ResourceFactory getFactory (String type)
    {
        return _factories.get(type);
    }

    void add (Resource rsrc)
    {
        Preconditions.checkNotNull(rsrc._group,
            "Resource doesn't belong to a group [rsrc=%s]", rsrc);
        Preconditions.checkState(rsrc.state() == Loadable.State.LOADED, "Resource must be loaded");
        Resource old = _resources.put(rsrc.name, rsrc);
        Preconditions.checkState(old == null,
            "A resource with that name already exists [name=%s]", rsrc.name);
    }

    void add (Iterable<Resource> rsrcs)
    {
        for (Resource rsrc : rsrcs) {
            add(rsrc);
        }
    }

    protected Map<String, ResourceFactory> _factories = Maps.newHashMap();
    protected Map<String, Resource> _resources = Maps.newHashMap();
}
