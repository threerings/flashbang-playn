//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.List;
import com.google.common.collect.Lists;

import com.threerings.flashbang.desc.BasicNamedDataDesc;
import playn.core.Image;
import playn.core.Json;
import playn.core.PlayN;

import tripleplay.util.JsonUtil;

public class ImageDesc extends BasicNamedDataDesc
{
    public List<RectangleDesc> frameRects; // nullable

    public String filename ()
    {
        return name;
    }

    public Image image ()
    {
        if (_image == null) {
            _image = PlayN.assetManager().getImage(filename());
        }
        return _image;
    }

    public void fromJson (Json.Object json)
    {
        super.fromJson(json);

        if (json.getObject("frameRects") != null) {
            frameRects = Lists.newArrayList();
            for (Json.Object jsonRect : JsonUtil.getArrayObjects(json, "frameRects")) {
                RectangleDesc rect = new RectangleDesc();
                rect.fromJson(jsonRect);
                frameRects.add(rect);
            }
        }
    }

    protected Image _image;
}
