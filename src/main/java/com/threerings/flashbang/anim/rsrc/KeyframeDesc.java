//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.rsrc;

import playn.core.Json;

import tripleplay.util.Interpolator;
import tripleplay.util.JsonUtil;

import com.threerings.flashbang.desc.DataDesc;

public class KeyframeDesc
    implements DataDesc
{
    public int frameIdx;
    public Interpolator interp;

    public float x;
    public float y;
    public float scaleX;
    public float scaleY;
    public float rotation;
    public boolean visible;
    public float alpha;

    public void fromJson (Json.Object json)
    {
        frameIdx = JsonUtil.requireInt(json, "frameIdx");
        interp = JsonUtil.requireEnum(json, "interp", InterpolatorType.class).interp;

        x = JsonUtil.requireFloat(json, "x");
        y = JsonUtil.requireFloat(json, "y");
        scaleX = JsonUtil.requireFloat(json, "scaleX");
        scaleY = JsonUtil.requireFloat(json, "scaleY");
        rotation = JsonUtil.requireFloat(json, "rotation");
        visible = JsonUtil.requireBoolean(json, "visible");
        alpha = JsonUtil.requireFloat(json, "alpha");
    }

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
        return (next != null ? next.frameIdx - 1 : Integer.MAX_VALUE);
    }

    KeyframeDesc next;
}
