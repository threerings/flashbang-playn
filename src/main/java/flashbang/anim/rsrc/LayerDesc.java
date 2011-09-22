//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;

import playn.core.Layer;

import flashbang.desc.DataDesc;

public abstract class LayerDesc
    implements DataDesc
{
    public String name;

    public Layer build (String selectorPrefix, Map<String, Layer> layerLookup)
    {
        Preconditions.checkNotNull(selectorPrefix, "selectorPrefix must not be null");
        Layer layer = createLayer();

        String layerName = getSelector(selectorPrefix);
        if (layerName != null) {
            layerLookup.put(layerName, layer);
        }

        return layer;
    }

    protected abstract Layer createLayer ();

    protected String getSelector (String selectorPrefix)
    {
        return name != null ? selectorPrefix + name : null;
    }
}
