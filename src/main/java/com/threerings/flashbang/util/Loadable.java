//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.util;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public abstract class Loadable
{
    public enum State {
        NOT_LOADED,
        LOADING,
        LOADED,
        ERROR;
    }

    public static interface Callback {
        void done ();
        void error (Throwable err);
    }

    /**
     * Causes the Loadable to begin loading. If the Loadable is already loaded, the
     * callback will be called immediately.
     */
    public void load (Callback callback)
    {
        if (_state == State.LOADED) {
            callback.done();
            return;
        } else if (_state == State.ERROR) {
            callback.error(_loadError);
            return;
        }

        _callbacks.add(callback);
        if (_state == State.NOT_LOADED) {
            // load now!
            _state = State.LOADING;
            doLoad();
        }
    }

    public State state ()
    {
        return _state;
    }

    /**
     * Subclasses should implement loading logic here, and call loadComplete when loading has
     * finished.
     */
    protected abstract void doLoad ();

    protected void loadComplete (Throwable err)
    {
        Preconditions.checkState(_state == State.LOADING, "We weren't loading [state=%s]", _state);
        _loadError = err;
        _state = (err == null ? State.LOADED : State.ERROR);

        List<Callback> callbacks = _callbacks;
        _callbacks = Lists.newArrayList();
        for (Callback cb : callbacks) {
            if (_state == State.ERROR) {
                cb.error(_loadError);
            } else {
                cb.done();
            }
        }
    }

    protected List<Callback> _callbacks = Lists.newArrayList();
    protected State _state = State.NOT_LOADED;
    protected Throwable _loadError;
}
