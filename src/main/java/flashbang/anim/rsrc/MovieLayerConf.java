//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import playn.core.GroupLayer;
import playn.core.Layer;

public interface MovieLayerConf
{
    void build (GroupLayer parent, List<String> animationNames,
        Multimap<String, Animatable> animations);
    String name ();
    AnimConf animation (String name);
}
