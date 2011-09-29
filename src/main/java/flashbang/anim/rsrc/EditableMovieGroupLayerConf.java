//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import react.RList;

import playn.core.GroupLayer;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;

public class EditableMovieGroupLayerConf extends EditableMovieLayerConf
    implements MovieGroupLayerConf
{
    public final RList<EditableMovieLayerConf> children = RList.create();

    public EditableMovieGroupLayerConf (Json.Object obj) {
        super(obj);
        addChildren(obj);
    }

    protected void addChildren(Json.Object obj) {
        for (Json.Object child : obj.getArray("children", Json.Object.class)) {
            if (child.getString("type").equals("Group")) {
                children.add(new EditableMovieGroupLayerConf(child));
            } else {
                children.add(new EditableMovieImageLayerConf(child));
            }
        }
    }

    public EditableMovieGroupLayerConf (String name) {
        this.name.update(name);
    }

    public List<EditableMovieLayerConf> children () { return children; }

    @Override public Layer build (List<String> names, Multimap<String, Animatable> animations) {
        GroupLayer layer = PlayN.graphics().createGroupLayer();
        for (EditableMovieLayerConf child : children) {
            layer.add(child.build(names, animations));
        }
        return add(layer, names, animations);
    }
}
