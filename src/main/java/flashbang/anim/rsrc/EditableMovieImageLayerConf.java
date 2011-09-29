//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Multimap;

import playn.core.ImageLayer;
import playn.core.Json;
import playn.core.Layer;
import playn.core.PlayN;

import flashbang.rsrc.ImageResource;

public class EditableMovieImageLayerConf extends EditableMovieLayerConf implements MovieImageLayerConf
{
    public EditableMovieImageLayerConf () {}

    public EditableMovieImageLayerConf (Json.Object obj) {
        super(obj);
    }

    @Override public Layer build (List<String> names, Multimap<String, Animatable> animations) {
        if (_imageRsrc == null) {
            _imageRsrc = ImageResource.require(name());
        }
        return add(PlayN.graphics().createImageLayer(_imageRsrc.image()), names, animations);
    }

    protected ImageResource _imageRsrc;
}
