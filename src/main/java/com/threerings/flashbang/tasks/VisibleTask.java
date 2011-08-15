//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;
import com.threerings.flashbang.components.VisibleComponent;

public class VisibleTask
    implements ObjectTask
{
    public VisibleTask (boolean visible)
    {
        _toVisible = visible;
    }

    @Override
    public boolean update (float dt, GameObject obj)
    {
        Preconditions.checkArgument(obj instanceof VisibleComponent,
            "VisibleTask only operates on GameObjects that implement VisibleComponent");

        ((VisibleComponent) obj).setVisible(_toVisible);
        return true;
    }

    @Override
    public VisibleTask clone ()
    {
        return new VisibleTask(_toVisible);
    }

    protected final boolean _toVisible;
}
