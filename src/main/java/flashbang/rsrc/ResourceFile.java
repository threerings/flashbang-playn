//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.rsrc;

import com.google.common.base.Preconditions;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import tripleplay.util.JsonUtil;

import flashbang.Flashbang;

/**
 * A ResourceBatch that takes reads a set of resources from a JSON file and loads them
 */
public class ResourceFile extends ResourceBatch
{
    public ResourceFile (String filename, String group) {
        super(group);
        _filename = filename;
    }

    public ResourceFile (String filename) {
        this(filename, filename);
    }

    @Override public void doLoad () {
        // First, load the file
        PlayN.assetManager().getText(_filename, new ResourceCallback<String>() {
            @Override public void done (String text) {
                // Parse our Resources from JSON
                Json.Object json = PlayN.json().parse(text);
                for (Json.Object jsonRsrc : json.getArray("resources", Json.Object.class)) {
                    String type = JsonUtil.requireString(jsonRsrc, "type");
                    ResourceFactory factory = Flashbang.rsrcs().getFactory(type);
                    Preconditions.checkNotNull(factory,
                        "No ResourceFactory for Resource [type=%s]", type);
                    addInternal(factory.create(JsonUtil.requireString(jsonRsrc, "name"),
                        jsonRsrc));
                }

                // And load!
                ResourceFile.super.doLoad();
            }

            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    protected final String _filename;
}
