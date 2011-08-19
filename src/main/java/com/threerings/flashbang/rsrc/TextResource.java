//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.google.common.base.Preconditions;

import playn.core.PlayN;
import playn.core.ResourceCallback;

public class TextResource extends Resource<String>
{
    public TextResource (String path)
    {
        super(path);
    }

    @Override
    public void load (final ResourceCallback<? super String> callback)
    {
        Preconditions.checkState(_text == null, "Already loaded");
        PlayN.assetManager().getText(path, new ResourceCallback<String>() {
            @Override public void done (String rsrc) {
                _text = rsrc;
                callback.done(rsrc);
            }
            @Override public void error (Throwable err) {
                callback.error(err);
            }
        });
    }

    @Override
    public String get ()
    {
        return _text;
    }

    protected String _text;
}
