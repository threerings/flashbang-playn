//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.google.common.base.Preconditions;

import playn.core.PlayN;
import playn.core.ResourceCallback;
import playn.core.Sound;

public class SoundResource extends Resource<Sound>
{
    public SoundResource (String path)
    {
        super(path);
    }

    @Override
    public void load (final ResourceCallback<? super Sound> callback)
    {
        Preconditions.checkState(_sound == null, "Already loaded");
        _sound = PlayN.assetManager().getSound(path);
        // sounds currently load immediately
        callback.done(_sound);
    }

    @Override
    public Sound get ()
    {
        return _sound;
    }

    protected Sound _sound;
}
