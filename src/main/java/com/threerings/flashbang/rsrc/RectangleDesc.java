//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.threerings.flashbang.desc.BasicDataDesc;
import playn.core.Json;
import tripleplay.util.JsonUtil;

public class RectangleDesc extends BasicDataDesc
{
    public float x;
    public float y;
    public float width;
    public float height;

    @Override
    public void fromJson (Json.Object json)
    {
        x = JsonUtil.requireFloat(json, "x");
        y = JsonUtil.requireFloat(json, "y");
        width = JsonUtil.requireFloat(json, "width");
        height = JsonUtil.requireFloat(json, "height");
    }
}
