//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import playn.core.Json;
import tripleplay.util.Interpolator;
import tripleplay.util.JsonUtil;

public class KeyframeDesc
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
        frameIdx = json.getInt("frameIdx");
        interp = JsonUtil.getEnum(json, "interp", InterpolatorType.class).interp;

        x = (float) json.getNumber("x");
        y = (float) json.getNumber("y");
        scaleX = (float) json.getNumber("scaleX");
        scaleY = (float) json.getNumber("scaleY");
        rotation = (float) json.getNumber("rotation");
        visible = json.getBoolean("visible");
        alpha = (float) json.getNumber("alpha");
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
