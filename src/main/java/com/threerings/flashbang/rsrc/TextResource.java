//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.PlayN;
import playn.core.ResourceCallback;

public class TextResource extends Resource<String>
{
    public TextResource (String path)
    {
        super(path);
    }

    @Override
    protected void doLoad ()
    {
        PlayN.assetManager().getText(path, new ResourceCallback<String>() {
            @Override public void done (String rsrc) {
                _text = rsrc;
                loadComplete(null);
            }
            @Override public void error (Throwable err) {
                loadComplete(err);
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
