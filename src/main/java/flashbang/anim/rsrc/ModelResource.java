//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import playn.core.GroupLayer;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;

import react.RList;

import tripleplay.util.JsonUtil;

import flashbang.Flashbang;
import flashbang.rsrc.Resource;
import flashbang.rsrc.ResourceFactory;

public class ModelResource extends Resource
{
    public static final String TYPE = "model";
    public static final ResourceFactory FACTORY = new ResourceFactory() {
        @Override public Resource create (String name, Json.Object json) {
            RList<LayerDesc> layers = RList.create();
            for (Json.Object jsonLayer : json.getArray("layers", Json.Object.class)) {
                layers.add(LayerDesc.create(jsonLayer));
            }

            Json.Object jsonAnims = JsonUtil.requireObject(json, "anims");
            Map<String, ModelAnimDesc> anims = Maps.newHashMap();
            for (String animName : jsonAnims.getKeys()) {
                ModelAnimDesc anim = new ModelAnimDesc();
                anim.fromJson(jsonAnims.getObject(animName));
                anims.put(animName, anim);
            }

            String defaultAnimation = json.getString("defaultAnimation");

            return new ModelResource(name, layers, anims, defaultAnimation);
        }
    };

    public static ModelResource require (String name)
    {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkState(rsrc instanceof ModelResource,
            "Not a ModelResource [name=%s]", name);
        return (ModelResource) rsrc;
    }

    public final RList<LayerDesc> layers;
    public final Map<String, ModelAnimDesc> anims;
    public final String defaultAnimation; // Nullable

    public ModelResource (String name) {
        this(name, RList.<LayerDesc>create(), Maps.<String, ModelAnimDesc>newHashMap(),
            null);
    }

    public ModelResource (String name, RList<LayerDesc> layers, Map<String, ModelAnimDesc> anims,
        String defaultAnimation)
    {
        super(name);
        this.layers = layers;
        this.anims = anims;
        this.defaultAnimation = defaultAnimation;
    }

    public GroupLayer build (Map<String, Layer> layerLookup)
    {
        GroupLayer root = PlayN.graphics().createGroupLayer();
        for (LayerDesc layerDesc : layers) {
            root.add(layerDesc.build("", layerLookup));
        }
        return root;
    }

    public GroupLayer build ()
    {
        return build(Maps.<String, Layer>newHashMap());
    }

    @Override
    protected void doLoad ()
    {
        // nothing more to load
        loadComplete(null);
    }
}
