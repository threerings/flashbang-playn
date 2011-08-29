//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class JsonResource extends Resource
{
    public JsonResource (String path)
    {
        super(path);
    }

    @Override
    protected void doLoad ()
    {
        PlayN.assetManager().getText(path, new ResourceCallback<String>() {
            @Override public void done (String text) {
                try {
                    _json = PlayN.json().parse(text);
                } catch (Throwable err) {
                    loadComplete(err);
                }
                loadComplete(null);
            }
            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    public Json.Object get ()
    {
        return _json;
    }

    protected Json.Object _json;
}
