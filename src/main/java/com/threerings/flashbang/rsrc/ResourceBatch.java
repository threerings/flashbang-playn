//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import com.threerings.flashbang.Flashbang;
import com.threerings.flashbang.util.Loadable;
import com.threerings.flashbang.util.LoadableBatch;

/**
 * A LoadableBatch that loads a collection of resources and adds them all to the ResourceManager
 */
public class ResourceBatch extends LoadableBatch
{
    public ResourceBatch (String group)
    {
        super(false);
        _group = group;
    }

    @Override
    public void add (Loadable loadable)
    {
        Preconditions.checkArgument(loadable instanceof Resource,
            "ResourceBatch can only load Resources");

        Resource rsrc = (Resource) loadable;
        Resource old = _resources.put(rsrc.name, rsrc);
        Preconditions.checkState(old == null,
            "A resource with that name is already queued [name=%s]", rsrc.name);
        super.add(loadable);
    }

    public boolean containsResource (String path)
    {
        return _resources.containsKey(path);
    }

    @Override
    protected void loadComplete (Throwable err)
    {
        if (err == null) {
            try {
                Flashbang.rsrcs().add(_resources.values(), _group);
            } catch (Throwable addErr) {
                err = addErr;
            }
        }

        super.loadComplete(err);
    }

    protected final String _group;
    protected Map<String, Resource> _resources = Maps.newHashMap();
}
