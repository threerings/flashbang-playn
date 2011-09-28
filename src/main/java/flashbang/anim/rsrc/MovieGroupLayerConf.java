//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

public interface MovieGroupLayerConf extends MovieLayerConf
{
    List<? extends MovieLayerConf> children ();
}
