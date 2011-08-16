//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.Map;

import playn.core.Json;
import playn.core.Layer;

public abstract class LayerDesc
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

    public static LayerDesc create (Json.Object json)
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

        desc.fromJson(json);
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

    public void fromJson (Json.Object json)
    {
        name = json.getString("name");

        x = (float) json.getNumber("x");
        y = (float) json.getNumber("y");
        scaleX = (float) json.getNumber("scaleX");
        scaleY = (float) json.getNumber("scaleY");
        rotation = (float) json.getNumber("rotation");
        alpha = (float) json.getNumber("alpha");
        visible = json.getBoolean("visible");

        originX = (float) json.getNumber("originX");
        originY = (float) json.getNumber("originY");
        depth = (float) json.getNumber("depth");
    }

    protected abstract Layer createLayer ();

    protected String getSelector (String selectorPrefix)
    {
        return (name != null && selectorPrefix != null ? selectorPrefix + name : null);
    }
}
