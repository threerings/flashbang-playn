//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

import playn.core.Json;
import playn.core.Layer;

import react.RMap;
import react.Value;

public abstract class EditableMovieLayerConf implements MovieLayerConf
{
    public final Value<String> name = Value.create(null);

    public final RMap<String, EditableAnimConf> animations = RMap.create();

    public EditableMovieLayerConf () {}

    public EditableMovieLayerConf (Json.Object obj) {
        name.update(obj.getString("name"));
        Json.Object anims = obj.getObject("animations");
        for (String key : anims.keys()) {
            animations.put(key, new EditableAnimConf(anims.getObject(key)));
        }
    }

    @Override public String name () { return name.get(); }

    @Override public EditableAnimConf animation (String name) { return animations.get(name); }

    public void write (Json.Writer writer) {
        writer.object().value("name", name());
        writeType(writer);
        writer.object("animations");
        for (Map.Entry<String, EditableAnimConf> entry : animations.entrySet()) {
            entry.getValue().write(entry.getKey(), writer);
        }
        writer.end().end();
    }

    protected abstract void writeType (Json.Writer writer);

    protected Layer add (Layer layer, List<String> names, Multimap<String, Animatable> animations) {
        for (String name : names) {
            animations.put(name, new Animatable(layer, animation(name)));
        }
        return layer;
    }
}
