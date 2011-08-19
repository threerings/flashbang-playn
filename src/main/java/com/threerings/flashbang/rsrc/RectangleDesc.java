//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.threerings.flashbang.desc.DataDesc;

import playn.core.Json;
import tripleplay.util.JsonUtil;

public class RectangleDesc
    implements DataDesc
{
    public float x;
    public float y;
    public float width;
    public float height;

    public void fromJson (Json.Object json)
    {
        x = JsonUtil.requireFloat(json, "x");
        y = JsonUtil.requireFloat(json, "y");
        width = JsonUtil.requireFloat(json, "width");
        height = JsonUtil.requireFloat(json, "height");
    }
}
