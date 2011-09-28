//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import react.Value;

import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.PlayN;

import flashbang.rsrc.ImageResource;

public class EditableMovieImageLayerConf extends EditableMovieLayerConf implements MovieImageLayerConf
{
    public final Value<String> imagePath = Value.create(null);

    @Override public String imagePath () { return imagePath.get(); }

    @Override public Layer build (List<String> names, Multimap<String, Animatable> animations) {
        if (_imageRsrc == null) {
            _imageRsrc = ImageResource.require(imagePath());
        }
        return add(PlayN.graphics().createImageLayer(_imageRsrc.image()), names, animations);
    }

    protected ImageResource _imageRsrc;
}
