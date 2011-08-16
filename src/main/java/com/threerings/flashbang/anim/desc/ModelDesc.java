//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import playn.core.Json;
import tripleplay.util.JsonUtil;

public class ModelDesc
{
    public LayerDesc rootLayer;
    public Map<String, ModelAnimDesc> anims;

    public void fromJson (Json.Object json)
    {
        rootLayer = LayerDesc.create(json.getObject("rootLayer"));

        Json.Object jsonAnims = json.getObject("anims");
        Preconditions.checkNotNull(jsonAnims, "Missing anims");
        anims = Maps.newHashMap();
        for (String animName : JsonUtil.getKeys(jsonAnims)) {
            ModelAnimDesc anim = new ModelAnimDesc();
            anim.fromJson(jsonAnims.getObject(animName));
            anims.put(animName, anim);
        }
    }
}
