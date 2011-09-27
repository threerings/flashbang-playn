//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import playn.core.GroupLayer;
import playn.core.Layer;

import react.RMap;
import react.Value;

public abstract class EditableModelLayer implements ModelLayer
{
    public final Value<String> name = Value.create(null);

    public final RMap<String, EditableLayerAnimation> animations = RMap.create();

    @Override public String name () { return name.get(); }

    @Override public EditableLayerAnimation animation (String name) { return animations.get(name); }

    @Override public void build (GroupLayer parent, List<String> animationNames,
        Multimap<String, Animatable> animations) {
        Layer layer = createLayer();
        parent.add(layer);
        for (String animName : animationNames) {
            animations.put(animName, new Animatable(layer, animation(animName)));
        }
    }

    protected abstract Layer createLayer ();

}
