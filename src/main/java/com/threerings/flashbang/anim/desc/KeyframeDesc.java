//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import pythagoras.f.Point;
import pythagoras.f.Vector;
import tripleplay.util.Interpolator;

public class KeyframeDesc
{
    public int frameIdx;
    public Interpolator interp;

    public Point loc;
    public Vector scale;
    public float rotation;
    public boolean visible;
    public float alpha;

    public KeyframeDesc next ()
    {
        return next;
    }

    public boolean validForFrame (int idx)
    {
        return (idx >= frameIdx && idx <= endFrameIdx());
    }

    public int endFrameIdx ()
    {
        return (next != null ? next.frameIdx - 1 : frameIdx);
    }

    KeyframeDesc next;
}
