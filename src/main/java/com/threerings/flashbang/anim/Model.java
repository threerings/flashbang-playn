//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim;

import java.util.Map;

import com.google.common.collect.Maps;

import playn.core.Layer;

import com.threerings.flashbang.SceneObject;
import com.threerings.flashbang.anim.desc.ModelDesc;

public class Model extends SceneObject
{
    public Model (ModelDesc desc)
    {
        _desc = desc;
        _root = _desc.rootLayer.build("", _layerLookup);
    }

    public Layer getLayer (String selector)
    {
        return _layerLookup.get(selector);
    }

    @Override
    public Layer layer ()
    {
        return _root;
    }

    protected final ModelDesc _desc;
    protected final Map<String, Layer> _layerLookup = Maps.newHashMap();
    protected Layer _root;

}
