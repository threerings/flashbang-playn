//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import java.util.Map;

import com.google.common.collect.Maps;

import com.threerings.flashbang.desc.BasicNamedDataDesc;

import playn.core.Json;
import tripleplay.util.JsonUtil;

public class ModelDesc extends BasicNamedDataDesc
{
    public LayerDesc rootLayer;
    public Map<String, ModelAnimDesc> anims;
    public String defaultAnimation; // Nullable

    @Override
    public void fromJson (Json.Object json)
    {
        super.fromJson(json);

        rootLayer = LayerDesc.create(json.getObject("rootLayer"));

        Json.Object jsonAnims = JsonUtil.requireObject(json, "anims");
        anims = Maps.newHashMap();
        for (String animName : JsonUtil.getKeys(jsonAnims)) {
            ModelAnimDesc anim = new ModelAnimDesc();
            anim.fromJson(jsonAnims.getObject(animName));
            anims.put(animName, anim);
        }

        defaultAnimation = json.getString("defaultAnimation");
    }
}
