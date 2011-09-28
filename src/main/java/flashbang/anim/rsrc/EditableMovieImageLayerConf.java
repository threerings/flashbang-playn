//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;

import pythagoras.f.Rectangle;

import react.Value;

import flashbang.rsrc.ImageResource;

public class EditableMovieImageLayerConf extends EditableMovieLayerConf implements MovieImageLayerConf
{
    public final Value<String> imagePath = Value.create(null);

    @Override public String imagePath () { return imagePath.get(); }

    @Override protected Layer createLayer () {
        if (_imageRsrc == null) {
            _imageRsrc = ImageResource.require(imagePath());
        }
        ImageLayer layer = PlayN.graphics().createImageLayer(_imageRsrc.image());
        if (_imageRsrc.frameRects != null) {
            Rectangle srcRect = _imageRsrc.frameRects.get(0);
            layer.setSourceRect(srcRect.x, srcRect.y, srcRect.width, srcRect.height);
        }
        return layer;
    }

    protected ImageResource _imageRsrc;
}
