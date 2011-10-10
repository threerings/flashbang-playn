//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.objects;

import playn.core.Layer;

import flashbang.SceneObject;

public class SimpleSceneObject extends SceneObject
{
    public SimpleSceneObject (Layer layer)
    {
        _layer = layer;
    }

    @Override public Layer layer ()
    {
        return _layer;
    }

    protected Layer _layer;
}
