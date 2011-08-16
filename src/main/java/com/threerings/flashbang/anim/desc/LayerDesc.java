//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.Map;

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

    protected abstract Layer createLayer ();

    protected String getSelector (String selectorPrefix)
    {
        return (name != null && selectorPrefix != null ? selectorPrefix + name : null);
    }
}
