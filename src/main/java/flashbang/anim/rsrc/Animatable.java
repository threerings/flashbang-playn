//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import playn.core.Layer;

public class Animatable
{
    public final Layer layer;
    public final LayerAnimation animation;

    public Animatable (Layer layer, LayerAnimation animation) {
        this.layer = layer;
        this.animation = animation;
    }
}

