//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import react.RMap;
import react.Value;

import playn.core.Layer;

public abstract class EditableMovieLayerConf implements MovieLayerConf
{
    public final Value<String> name = Value.create(null);

    public final RMap<String, EditableAnimConf> animations = RMap.create();

    @Override public String name () { return name.get(); }

    @Override public EditableAnimConf animation (String name) { return animations.get(name); }

    protected Layer add (Layer layer, List<String> names, Multimap<String, Animatable> animations) {
        for (String name : names) {
            animations.put(name, new Animatable(layer, animation(name)));
        }
        return layer;
    }
}
