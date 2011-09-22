//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;
import java.util.Map;

import react.RList;
import react.RMap;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.PlayN;

public class EditableModelResource implements ModelResource
{
    public final RList<LayerDesc> layers = RList.create();
    public final RMap<String, EditableModelAnimation> animations = RMap.create();

    @Override public List<LayerDesc> layers () { return layers; }
    @Override public Map<String, EditableModelAnimation> animations () { return animations; }

    @Override public GroupLayer build (Map<String, Layer> layerLookup) {
        GroupLayer root = PlayN.graphics().createGroupLayer();
        for (LayerDesc layerDesc : layers) {
            root.add(layerDesc.build("", layerLookup));
        }
        return root;
    }
}
