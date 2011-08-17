//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim;

import java.util.Map;

import com.google.common.collect.Maps;

import playn.core.Layer;
import com.threerings.flashbang.SceneObject;
import com.threerings.flashbang.anim.desc.ModelAnimDesc;
import com.threerings.flashbang.anim.desc.ModelDesc;

public class Model extends SceneObject
{
    public Model (ModelDesc desc)
    {
        _desc = desc;
        _root = _desc.rootLayer.build("", _layerLookup);

        if (_desc.defaultAnimation != null) {
            playAnimation(_desc.defaultAnimation);
        }
    }

    public Layer getLayer (String selector)
    {
        return _layerLookup.get(selector);
    }

    public void playAnimation (String name)
    {
        ModelAnimDesc animDesc = _desc.anims.get(name);
        _animator = new AnimationController(this, animDesc);
    }

    @Override
    public Layer layer ()
    {
        return _root;
    }

    @Override
    protected void update (float dt)
    {
        super.update(dt);
        if (_animator != null) {
            _animator.update(dt);
        }
    }

    protected final ModelDesc _desc;
    protected final Map<String, Layer> _layerLookup = Maps.newHashMap();
    protected Layer _root;
    protected AnimationController _animator;

}
