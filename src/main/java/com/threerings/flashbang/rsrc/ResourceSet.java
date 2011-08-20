//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import com.threerings.flashbang.util.Loadable;
import com.threerings.flashbang.util.LoadableBatch;

public class ResourceSet extends LoadableBatch
{
    public ResourceSet (ResourceManager rm, boolean loadInSequence)
    {
        super(loadInSequence);
        _rm = rm;
    }

    public ResourceSet (ResourceManager rm)
    {
        this(rm, false);
    }

    @Override
    public void addLoadable (Loadable loadable)
    {
        if (loadable instanceof Resource) {
            Resource<?> rsrc = (Resource<?>) loadable;
            Resource<?> old = _resources.put(rsrc.path, rsrc);
            Preconditions.checkState(old == null,
                "A resource with that name is already queued [name=%s]", rsrc.path);
        }
        super.addLoadable(loadable);
    }

    public boolean containsResource (String name)
    {
        return _resources.containsKey(name);
    }

    @Override
    protected void loadComplete (Throwable err)
    {
        if (err == null) {
            try {
                _rm.addAll(_resources.values());
            } catch (Throwable addErr) {
                err = addErr;
            }
        }

        super.loadComplete(err);
    }

    protected final ResourceManager _rm;
    protected Map<String, Resource<?>> _resources = Maps.newHashMap();
}
