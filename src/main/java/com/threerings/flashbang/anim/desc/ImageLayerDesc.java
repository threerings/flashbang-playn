//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import playn.core.Image;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;

public class ImageLayerDesc extends LayerDesc
{
    public String imageName;

    @Override
    public void fromJson (Json.Object json)
    {
        super.fromJson(json);
        imageName = json.getString("imageName");
    }

    @Override
    protected Layer createLayer ()
    {
        Image image = PlayN.assetManager().getImage(imageName);
        Preconditions.checkState(image != null, "invalid image [name=%s]", imageName);

        return PlayN.graphics().createImageLayer(image);
    }

}
