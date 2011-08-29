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
 * Loads a group of Resources and adds them all to the ResourceManager
 */
public class ResourceBatch extends Loadable
{
    public ResourceBatch (String group)
    {
        _group = group;
    }

    public void add (Resource rsrc)
    {
        Preconditions.checkState(rsrc._group == null,
            "Resource already belongs to a group [rsrc=%s]", rsrc);
        Preconditions.checkState(_state == State.NOT_LOADED,
            "Can't add Resources to a group that's loading or loaded");

        Resource old = _resources.put(rsrc.name, rsrc);
        rsrc._group = _group;
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
                Flashbang.rsrcs().add(_resources.values());
            } catch (Throwable addErr) {
                err = addErr;
            }
        }

        super.loadComplete(err);
    }

    protected final String _group;
    protected Map<String, Resource> _resources = Maps.newHashMap();
}
