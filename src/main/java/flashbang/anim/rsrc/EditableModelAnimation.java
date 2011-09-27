//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import react.RList;

public class EditableModelAnimation implements ModelAnimation
{
    public final RList<EditableLayerAnimation> layers = RList.create();

    @Override public List<EditableLayerAnimation> layers () { return layers; }

    @Override public int frames () {
        int max = 1;
        for (LayerAnimation layer : layers) {
            max = Math.max(layer.frames(), max);
        }
        return max;
    }
}
