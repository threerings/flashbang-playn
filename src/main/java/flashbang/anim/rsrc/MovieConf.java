//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import playn.core.Layer;

import flashbang.anim.Movie;
import flashbang.rsrc.Resource;

public interface MovieConf
{
    List<? extends MovieLayerConf> children ();
    Movie build ();
}
