//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import playn.core.Layer;

import flashbang.rsrc.Resource;

public interface ModelResource
{
    List<LayerDesc> layers ();
    Map<String, ? extends ModelAnimation> animations ();
    GroupLayer build (Map<String, Layer> layers);
}
