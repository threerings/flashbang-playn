//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;
import tripleplay.util.JsonUtil;

public class ImageLayerDesc extends LayerDesc
{
    public ImageDesc image;

    @Override
    public void fromJson (Json.Object json, AssetPackageDesc assets)
    {
        super.fromJson(json, assets);
        image = assets.images.requireData(JsonUtil.requireString(json, "image"));
    }

    @Override
    protected Layer createLayer ()
    {
        return PlayN.graphics().createImageLayer(image.image());
    }

}
