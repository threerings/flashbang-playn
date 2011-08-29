//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.PlayN;
import playn.core.Sound;

public class SoundResource extends Resource
{
    public SoundResource (String path)
    {
        super(path);
    }

    @Override
    protected void doLoad ()
    {
        _sound = PlayN.assetManager().getSound(path);
        // sounds currently load immediately
        loadComplete(null);
    }

    public Sound get ()
    {
        return _sound;
    }

    protected Sound _sound;
}
