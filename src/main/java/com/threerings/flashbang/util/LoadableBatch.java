//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.util;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class LoadableBatch extends Loadable
{
    /**
     * Creates a new LoadableBatch.
     *
     * @param loadInSequence if true, loads all Loadables one by one (useful if there are
     * dependencies between the Loadables). Otherwise, loads all Loadables simultaneously.
     */
    public LoadableBatch (boolean loadInSequence)
    {
        _loadInSequence = loadInSequence;
    }

    public LoadableBatch ()
    {
        this(false);
    }

    public void addLoadable (Loadable loadable)
    {
        Preconditions.checkState(_state == State.NOT_LOADED, "Can't add Loadables now [state=%s]",
            _state);
        _allLoadables.add(loadable);
    }

    @Override
    protected void doLoad ()
    {
        if (_allLoadables.isEmpty()) {
            // We don't have anything to load
            loadComplete(null);
            return;
        }

        for (Loadable loadable : _allLoadables) {
            load1Loadable(loadable);
            // don't continue if the load operation has been canceled/errored,
            // or if we're loading in sequence
            if (_state != State.LOADING || _loadInSequence) {
                break;
            }
        }
    }

    protected void load1Loadable (final Loadable loadable)
    {
        loadable.load(new Callback() {
            @Override public void done () {
                loadableLoaded(loadable, null);

            }
            @Override public void error (Throwable err) {
                loadableLoaded(loadable, err);
            }
        });
    }

    protected void loadableLoaded (Loadable loadable, Throwable err)
    {
        // We may have already errored out, in which case, ignore
        if (_state == State.ERROR) {
            return;
        }

        if (err != null) {
            loadComplete(err);

        } else {
            _loadedLoadables.add(loadable);
            if (_loadedLoadables.size() == _allLoadables.size()) {
                // We finished loading
                loadComplete(null);
            } else if (_loadInSequence) {
                // We have more to load
                load1Loadable(_allLoadables.get(_loadedLoadables.size()));
            }
        }
    }

    protected boolean _loadInSequence;
    protected List<Loadable> _allLoadables = Lists.newArrayList();
    protected List<Loadable> _loadedLoadables = Lists.newArrayList();
}
