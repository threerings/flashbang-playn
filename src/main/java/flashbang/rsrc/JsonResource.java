//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.rsrc;

import com.google.common.base.Preconditions;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import flashbang.Flashbang;

public class JsonResource extends Resource
{
    public static JsonResource require (String name)
    {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkState(rsrc instanceof JsonResource,
            "Not a JsonResource [name=%s]", name);
        return (JsonResource)rsrc;
    }

    public JsonResource (String path) {
        super(path);
    }

    public Json.Object json () { return _json; }

    @Override protected void doLoad () {
        PlayN.assetManager().getText(name, new ResourceCallback<String>() {
            @Override public void done (String text) {
                _json = PlayN.json().parse(text);
                loadComplete(null);
            }

            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });

    }

    protected Json.Object _json;
}
