//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import java.util.Map;

import com.google.common.collect.Maps;

import playn.core.GroupLayer;
import playn.core.Layer;

import flashbang.SceneObject;
import flashbang.anim.rsrc.ModelAnimDesc;
import flashbang.anim.rsrc.ModelResource;

public class Model extends SceneObject
{
    public Model (ModelResource rsrc)
    {
        _rsrc = rsrc;
        _root = _rsrc.build(_layerLookup);

        if (_rsrc.defaultAnimation != null) {
            playAnimation(_rsrc.defaultAnimation);
        }
    }

    public Model (String rsrcName)
    {
        this(ModelResource.require(rsrcName));
    }

    public Layer getLayer (String selector)
    {
        return _layerLookup.get(selector);
    }

    public AnimationController playAnimation (String name)
    {
        ModelAnimDesc animDesc = _rsrc.anims.get(name);
        _animator = new AnimationController(this, animDesc);
        return _animator;
    }

    @Override
    public GroupLayer layer ()
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

    protected final ModelResource _rsrc;
    protected final Map<String, Layer> _layerLookup = Maps.newHashMap();
    protected final GroupLayer _root;
    protected AnimationController _animator;

}
