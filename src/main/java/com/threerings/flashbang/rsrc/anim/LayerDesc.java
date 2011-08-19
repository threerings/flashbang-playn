//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import java.util.Map;

import com.threerings.flashbang.desc.DataDesc;
import playn.core.Json;
import playn.core.Layer;
import tripleplay.util.JsonUtil;

public abstract class LayerDesc
    implements DataDesc
{
    public String name;

    public float x;
    public float y;
    public float scaleX;
    public float scaleY;
    public float rotation;
    public float alpha;
    public boolean visible;

    public float originX;
    public float originY;
    public float depth;

    public static LayerDesc create (Json.Object json, AssetPackageDesc assets)
    {
        LayerDesc desc = null;

        String typeName = json.getString("type");
        if ("ImageLayer".equals(typeName)) {
            desc = new ImageLayerDesc();
        } else if ("GroupLayer".equals(typeName)) {
            desc = new GroupLayerDesc();
        } else {
            throw new RuntimeException("Unrecognized layer type [type=" + typeName + "]");
        }

        desc.fromJson(json, assets);
        return desc;
    }

    public Layer build (String selectorPrefix, Map<String, Layer> layerLookup)
    {
        Layer layer = createLayer();
        layer.setTranslation(x, y);
        layer.setScale(scaleX, scaleY);
        layer.setRotation(rotation);
        layer.setAlpha(alpha);
        layer.setVisible(visible);
        layer.setOrigin(originX, originY);
        layer.setDepth(depth);

        String layerName = getSelector(selectorPrefix);
        if (layerName != null) {
            layerLookup.put(layerName, layer);
        }

        return layer;
    }

    public void fromJson (Json.Object json, AssetPackageDesc assets)
    {
        name = json.getString("name");

        x = JsonUtil.requireFloat(json, "x");
        y = JsonUtil.requireFloat(json, "y");
        scaleX = JsonUtil.requireFloat(json, "scaleX");
        scaleY = JsonUtil.requireFloat(json, "scaleY");
        rotation = JsonUtil.requireFloat(json, "rotation");
        alpha = JsonUtil.requireFloat(json, "alpha");
        visible = JsonUtil.requireBoolean(json, "visible");

        originX = JsonUtil.requireFloat(json, "originX");
        originY = JsonUtil.requireFloat(json, "originY");
        depth = JsonUtil.requireFloat(json, "depth");
    }

    protected abstract Layer createLayer ();

    protected String getSelector (String selectorPrefix)
    {
        return (name != null && selectorPrefix != null ? selectorPrefix + name : null);
    }
}
