//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import playn.core.Layer;

import com.google.common.base.Preconditions;

import flashbang.GameObject;
import flashbang.ObjectTask;
import flashbang.components.DepthComponent;

public class DepthTask extends ObjectTask
{
    public DepthTask (Layer layer, float depth)
    {
        _layer = layer;
        _toDepth = depth;
    }

    public DepthTask (float depth)
    {
        this(null, depth);
    }

    @Override public void init (GameObject obj)
    {
        if (_layer != null) {
            _target = new LayerWrapper(_layer);
        } else {
            Preconditions.checkArgument(obj instanceof DepthComponent,
                "DepthTask only operates on GameObjects that implement DepthComponent");
            _target = (DepthComponent) obj;
        }
    }

    @Override public boolean update (float dt)
    {
        _target.setDepth(_toDepth);
        return true;
    }

    @Override public ObjectTask clone ()
    {
        return new DepthTask(_layer, _toDepth);
    }

    protected final Layer _layer;
    protected final float _toDepth;

    protected DepthComponent _target;
}
