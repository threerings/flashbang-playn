//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import pythagoras.f.Rectangle;

import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;

import flashbang.rsrc.ImageResource;

public class ImageLayerDesc extends LayerDesc
{
    public String imageName;

    public ImageResource imageRsrc ()
    {
        if (_imageRsrc == null) {
            _imageRsrc = ImageResource.require(imageName);
        }
        return _imageRsrc;
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
