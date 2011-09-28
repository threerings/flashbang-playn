//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import react.RList;
import react.Value;

import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.PlayN;

import flashbang.anim.Movie;

public class EditableMovieConf implements MovieConf
{
    public final EditableMovieGroupLayerConf root = new EditableMovieGroupLayerConf("root");

    // TODO - listen for add and remove to update children
    public final RList<String> animations = RList.create(Lists.newArrayList(DEFAULT_ANIMATION));

    public final Value<String> animation = Value.create(DEFAULT_ANIMATION);

    public void add (EditableMovieGroupLayerConf parent, EditableMovieLayerConf child) {
        for (String name : animations) {
            if (child.animations.containsKey(name)) continue;
            child.animations.put(name, new EditableAnimConf());
        }
        parent.children.add(child);
    }

    @Override public Movie build () {
        GroupLayer root = PlayN.graphics().createGroupLayer();
        Multimap<String, Animatable> animations = ArrayListMultimap.create();
        for (MovieLayerConf child : this.root.children) {
            root.add(child.build(this.animations, animations));
        }
        return new Movie(root, animations);
    }
}
