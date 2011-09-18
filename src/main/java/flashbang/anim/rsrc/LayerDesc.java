//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;

import playn.core.Json;
import playn.core.Layer;

import tripleplay.util.JsonUtil;

import flashbang.desc.DataDesc;

public abstract class LayerDesc
    implements DataDesc
{
    public String name;

    public float x = 0;
    public float y = 0;
    public float scaleX = 1;
    public float scaleY = 1;
    public float rotation = 0;
    public float alpha = 1;
    public boolean visible = true;

    public float originX = 0;
    public float originY = 0;
    public float depth = 0;

    public static LayerDesc create (Json.Object json)
    {
        LayerDesc desc;

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
        Preconditions.checkNotNull(selectorPrefix, "selectorPrefix must not be null");
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
        name = JsonUtil.getString(json, "name", null);

        x = JsonUtil.getFloat(json, "x", 0);
        y = JsonUtil.getFloat(json, "y", 0);
        scaleX = JsonUtil.getFloat(json, "scaleX", 1);
        scaleY = JsonUtil.getFloat(json, "scaleY", 1);
        rotation = JsonUtil.getFloat(json, "rotation", 0);
        alpha = JsonUtil.getFloat(json, "alpha", 1);
        visible = JsonUtil.getBoolean(json, "visible", true);

        originX = JsonUtil.getFloat(json, "originX", 0);
        originY = JsonUtil.getFloat(json, "originY", 0);
        depth = JsonUtil.getFloat(json, "depth", 0);
    }

    protected abstract Layer createLayer ();

    protected String getSelector (String selectorPrefix)
    {
        return name != null ? selectorPrefix + name : null;
    }
}
