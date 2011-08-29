//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import com.threerings.flashbang.Flashbang;
import com.threerings.flashbang.rsrc.Resource;
import com.threerings.flashbang.rsrc.ResourceFactory;
import playn.core.Json;
import tripleplay.util.JsonUtil;

public class ModelResource extends Resource
{
    public static final String TYPE = "model";
    public static final ResourceFactory FACTORY = new ResourceFactory() {
        @Override public Resource create (String name, Json.Object json) {
            LayerDesc rootLayer = LayerDesc.create(json.getObject("rootLayer"));

            Json.Object jsonAnims = JsonUtil.requireObject(json, "anims");
            Map<String, ModelAnimDesc> anims = Maps.newHashMap();
            for (String animName : JsonUtil.getKeys(jsonAnims)) {
                ModelAnimDesc anim = new ModelAnimDesc();
                anim.fromJson(jsonAnims.getObject(animName));
                anims.put(animName, anim);
            }

            String defaultAnimation = json.getString("defaultAnimation");

            return new ModelResource(name, rootLayer, anims, defaultAnimation);
        }
    };

    public static ModelResource require (String name)
    {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkState(rsrc instanceof ModelResource,
            "Not a ModelResource [name=%s]", name);
        return (ModelResource) rsrc;
    }

    public final LayerDesc rootLayer;
    public final Map<String, ModelAnimDesc> anims;
    public final String defaultAnimation; // Nullable

    public ModelResource (String name, LayerDesc rootLayer, Map<String, ModelAnimDesc> anims,
        String defaultAnimation)
    {
        super(name);
        this.rootLayer = rootLayer;
        this.anims = anims;
        this.defaultAnimation = defaultAnimation;
    }

    @Override
    protected void doLoad ()
    {
        // nothing more to load
        loadComplete(null);
    }
}
