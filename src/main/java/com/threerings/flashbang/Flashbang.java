//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.rsrc.ResourceManager;

public class Flashbang
{
    public static FlashbangApp app ()
    {
        return _app;
    }

    public static ResourceManager rsrcs ()
    {
        return _rsrcs;
    }

    static void registerApp (FlashbangApp app)
    {
        Preconditions.checkState(_app == null, "A FlashbangApp already exists");
        _app = app;
    }

    protected static FlashbangApp _app;
    protected static ResourceManager _rsrcs = new ResourceManager();
}
