//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
import com.threerings.flashbang.components.VisibleComponent;

public class VisibleTask extends ObjectTask
{
    public VisibleTask (boolean visible)
    {
        _toVisible = visible;
    }

    @Override
    public void init (GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof VisibleComponent,
            "VisibleTask only operates on GameObjects that implement VisibleComponent");
        _target = (VisibleComponent)obj;
    }

    @Override
    public boolean update (float dt)
    {
        _target.setVisible(_toVisible);
        return true;
    }

    @Override
    protected ObjectTask createClone ()
    {
        return new VisibleTask(_toVisible);
    }

    protected VisibleComponent _target;

    protected final boolean _toVisible;
}
