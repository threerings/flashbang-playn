//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;
import tripleplay.util.JsonUtil;

import com.google.common.collect.Lists;


public class GroupLayerDesc extends LayerDesc
{
    public List<LayerDesc> children;

    @Override
    public void fromJson (Json.Object json)
    {
        super.fromJson(json);

        children = Lists.newArrayList();
        for (Json.Object jsonChild : JsonUtil.requireArrayObjects(json, "children")) {
            children.add(LayerDesc.create(jsonChild));
        }
    }

    @Override
    public Layer build (String layerNamePrefix, Map<String, Layer> layerNameMap)
    {
        GroupLayer layer = (GroupLayer) super.build(layerNamePrefix, layerNameMap);
        layerNamePrefix = getSelector(layerNamePrefix) + "/";
        for (LayerDesc childDesc : children) {
            layer.add(childDesc.build(layerNamePrefix, layerNameMap));
        }
        return layer;
    }

    @Override
    protected Layer createLayer ()
    {
        return PlayN.graphics().createGroupLayer();
    }

}
