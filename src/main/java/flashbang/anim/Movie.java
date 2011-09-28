//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import com.google.common.collect.Multimap;

import playn.core.GroupLayer;
import playn.core.Layer;

import flashbang.SceneObject;
import flashbang.anim.rsrc.Animatable;

public class Movie extends SceneObject
{
    public Movie (GroupLayer root, Multimap<String, Animatable> animations)
    {
        _root = root;
        _animations = animations;
    }

    public AnimationController play (String name)
    {
        _animator = new AnimationController(framerate(), _animations.get(name));
        return _animator;
    }

    public float framerate () { return 30; }

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

    protected final Multimap<String, Animatable> _animations;
    protected final GroupLayer _root;
    protected AnimationController _animator;
}
