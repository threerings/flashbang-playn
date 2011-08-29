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
public class ResourceGroup extends Loadable
{
    public ResourceGroup (String name)
    {
        _name = name;
    }

    public void add (Resource rsrc)
    {
        Resource old = _resources.put(rsrc.name, rsrc);
        rsrc._group = _name;
        Preconditions.checkState(old == null,
            "A resource with that name is already queued [name=%s]", rsrc.name);
    }

    public boolean contains (String name)
    {
        return _resources.containsKey(name);
    }

    @Override
    protected void doLoad ()
    {
        LoadableBatch batch = new LoadableBatch();
        for (Resource rsrc : _resources.values()) {
            batch.add(rsrc);
        }
        batch.load(new Callback() {
            @Override public void done () {
                loadComplete(null);
            }
            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    @Override
    protected void loadComplete (Throwable err)
    {
        if (err == null) {
            try {
                Flashbang.rsrcs().add(_resources.values(), _name);
            } catch (Throwable addErr) {
                err = addErr;
            }
        }

        super.loadComplete(err);
    }

    protected final String _name;
    protected Map<String, Resource> _resources = Maps.newHashMap();
}
