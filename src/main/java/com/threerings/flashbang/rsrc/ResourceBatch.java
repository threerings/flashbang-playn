//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import playn.core.ResourceCallback;

public class ResourceBatch
{
    public enum State {
        NOT_LOADED,
        LOADING,
        LOADED,
        ERROR;
    }

    public void addResource (Resource<?> rsrc)
    {
        Preconditions.checkState(_state == State.NOT_LOADED, "Can't add resources [curState=%s]",
            _state);

        Resource<?> old = _resourcesByPath.put(rsrc.path, rsrc);
        Preconditions.checkState(old == null, "Duplicate resources [name=%s]", rsrc.path);
    }

    public Resource<?> getResource (String path)
    {
        return _resourcesByPath.get(path);
    }

    public void load (ResourceCallback<ResourceBatch> callback)
    {
        if (_state == State.LOADED) {
            callback.done(this);
            return;
        } else if (_state == State.ERROR) {
            callback.error(_loadError);
            return;
        }

        _callbacks.add(callback);
        if (_state == State.NOT_LOADED) {
            // load now!
            _state = State.LOADING;

            for (Resource<?> rsrc : _resourcesByPath.values()) {
                load1Resource(rsrc);
            }
        }
    }

    public State state ()
    {
        return _state;
    }

    protected void load1Resource (final Resource<?> rsrc)
    {
        rsrc.load(new ResourceCallback<Object>() {
            @Override public void done (Object o) {
                rsrcLoaded(rsrc, null);
            }
            @Override public void error (Throwable err) {
                rsrcLoaded(rsrc, err);
            }
        });
    }

    protected void rsrcLoaded (Resource<?> rsrc, Throwable err)
    {
        // We may have already errored out, in which case, ignore
        if (_state == State.ERROR) {
            return;
        }

        if (err != null) {
            _loadError = err;
            _state = State.ERROR;
            loadComplete();

        } else {
            ++_numLoadedResources;
            Preconditions.checkState(_numLoadedResources <= _resourcesByPath.size());

            // Are we done?
            if (_numLoadedResources == _resourcesByPath.size()) {
                _state = State.LOADED;
                loadComplete();
            }
        }
    }

    protected void loadComplete ()
    {
        List<ResourceCallback<ResourceBatch>> callbacks = _callbacks;
        _callbacks = Lists.newArrayList();
        for (ResourceCallback<ResourceBatch> cb : callbacks) {
            if (_state == State.ERROR) {
                cb.error(_loadError);
            } else {
                cb.done(this);
            }
        }
    }

    protected List<ResourceCallback<ResourceBatch>> _callbacks = Lists.newArrayList();
    protected Map<String, Resource<?>> _resourcesByPath = Maps.newHashMap();
    protected State _state;
    protected Throwable _loadError;
    protected int _numLoadedResources;
}
