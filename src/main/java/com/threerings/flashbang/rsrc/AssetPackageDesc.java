//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.Map;

import playn.core.Json;
import playn.core.Json.Object;
import tripleplay.util.JsonUtil;

import com.threerings.flashbang.desc.BasicDataMgr;
import com.threerings.flashbang.desc.DataDesc;
import com.threerings.flashbang.desc.DataMgr;

public class AssetPackageDesc
    implements DataDesc
{
    public BasicDataMgr<ImageDesc> images;

    @Override
    public void fromJson (Object json)
    {
        images = new BasicDataMgr<ImageDesc>(ImageDesc.class);
        for (Json.Object imageJson : JsonUtil.requireArrayObjects(json, "images")) {
            ImageDesc image = new ImageDesc();
            image.fromJson(imageJson);
            images.addData(image);
        }
    }

    @Override
    public void resolveRefs (Map<Class<?>, DataMgr<?>> mgrs)
    {
        images.resolveRefs(mgrs);
    }
}
