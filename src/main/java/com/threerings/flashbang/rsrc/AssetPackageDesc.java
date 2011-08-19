//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.Json;
import playn.core.Json.Object;
import tripleplay.util.JsonUtil;

import com.threerings.flashbang.desc.BasicDataMgr;
import com.threerings.flashbang.desc.DataDesc;
import com.threerings.flashbang.rsrc.anim.ModelDesc;

public class AssetPackageDesc
    implements DataDesc
{
    public BasicDataMgr<ImageDesc> images;
    public BasicDataMgr<ModelDesc> models;

    public void fromJson (Object json)
    {
        images = new BasicDataMgr<ImageDesc>(ImageDesc.class);
        for (Json.Object imageJson : JsonUtil.requireArrayObjects(json, "images")) {
            ImageDesc image = new ImageDesc();
            image.fromJson(imageJson);
            images.addData(image);
        }

        models = new BasicDataMgr<ModelDesc>(ModelDesc.class);
        for (Json.Object modelJson : JsonUtil.requireArrayObjects(json, "models")) {
            ModelDesc model = new ModelDesc();
            model.fromJson(modelJson);
            models.addData(model);
        }
    }
}
