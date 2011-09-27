//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import playn.core.Layer;

import com.google.common.base.Preconditions;

import flashbang.GameObject;
import flashbang.ObjectTask;
import flashbang.components.VisibleComponent;

public class VisibleTask extends ObjectTask
{
    public VisibleTask (Layer layer, boolean visible)
    {
        _layer = layer;
        _visible = visible;
    }

    public VisibleTask (boolean visible)
    {
        this(null, visible);
    }

    @Override
    public void init (GameObject obj)
    {
        if (_layer != null) {
            _target = new LayerWrapper(_layer);
        } else {
            Preconditions.checkArgument(obj instanceof VisibleComponent,
                "VisibleTask only operates on GameObjects that implement VisibleComponent");
            _target = (VisibleComponent) obj;
        }
    }

    @Override
    public boolean update (float dt)
    {
        _target.setVisible(_visible);
        return true;
    }

    @Override
    public ObjectTask clone ()
    {
        return new VisibleTask(_layer, _visible);
    }

    protected final Layer _layer;
    protected final boolean _visible;

    protected VisibleComponent _target;
}
