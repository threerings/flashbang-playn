//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.Flashbang;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import tripleplay.util.JsonUtil;

/**
 * A ResourceBatch that takes reads a set of resources from a JSON file and loads them
 */
public class FileResourceBatch extends ResourceBatch
{
    public FileResourceBatch (String filename, String group)
    {
        super(group);
        _filename = filename;
    }

    @Override
    public void doLoad ()
    {
        // First, load the file
        PlayN.assetManager().getText(_filename, new ResourceCallback<String>() {
            @Override public void done (String text) {
                // Parse our Resources from JSON
                try {
                    Json.Object json = PlayN.json().parse(text);
                    for (Json.Object jsonRsrc : JsonUtil.getArrayObjects(json, "resources")) {
                        String type = JsonUtil.requireString(jsonRsrc, "type");
                        ResourceFactory factory = Flashbang.rsrcs().getFactory(type);
                        Preconditions.checkNotNull(factory,
                            "No ResourceFactory for Resource [type=%s]", type);
                        add(factory.create(JsonUtil.requireString(jsonRsrc, "name"), jsonRsrc));

                    }
                } catch (Throwable err) {
                    loadComplete(err);
                }

                // And load!
                FileResourceBatch.super.doLoad();
            }

            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    protected final String _filename;
}
