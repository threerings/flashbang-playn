//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
import com.threerings.flashbang.components.DepthComponent;

public class DepthTask
    implements ObjectTask
{
    public DepthTask (float depth)
    {
        _toDepth = depth;
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof DepthComponent,
            "DepthTask only operates on GameObjects that implement DepthComponent");

        ((DepthComponent) obj).setDepth(_toDepth);
        return true;
    }

    @Override
    public DepthTask clone ()
    {
        return new DepthTask(_toDepth);
    }

    protected final float _toDepth;
}
