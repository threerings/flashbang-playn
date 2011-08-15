//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
import com.threerings.flashbang.components.DepthComponent;

public class DepthTask extends ObjectTask
{
    public DepthTask (float depth)
    {
        _toDepth = depth;
    }

    @Override
    public void init (GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof DepthComponent,
            "DepthTask only operates on GameObjects that implement DepthComponent");
        _target = (DepthComponent)obj;
    }

    @Override
    public boolean update (float dt)
    {
        _target.setDepth(_toDepth);
        return true;
    }

    @Override
    public ObjectTask clone ()
    {
        return new DepthTask(_toDepth);
    }

    protected DepthComponent _target;

    protected final float _toDepth;
}
