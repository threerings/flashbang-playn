//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.rsrc;

import com.threerings.flashbang.Flashbang;
import com.threerings.flashbang.rsrc.ImageResource;

import playn.core.ImageLayer;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;
import pythagoras.f.Rectangle;
import tripleplay.util.JsonUtil;

public class ImageLayerDesc extends LayerDesc
{
    public String imageName;

    public ImageResource imageRsrc ()
    {
        if (_imageRsrc == null) {
            _imageRsrc = (ImageResource) Flashbang.rsrcs().requireResource(imageName);
        }
        return _imageRsrc;
    }

    @Override
    public void fromJson (Json.Object json)
    {
        super.fromJson(json);
        imageName = JsonUtil.requireString(json, "image");
    }

    @Override
    protected Layer createLayer ()
    {
        ImageResource imageRsrc = imageRsrc();
        ImageLayer layer = PlayN.graphics().createImageLayer(imageRsrc.image());
        if (imageRsrc.frameRects != null) {
            Rectangle srcRect = imageRsrc.frameRects.get(0);
            layer.setSourceRect(srcRect.x, srcRect.y, srcRect.width, srcRect.height);
        }
        return layer;
    }

    protected ImageResource _imageRsrc;
}
