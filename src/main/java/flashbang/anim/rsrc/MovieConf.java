//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import flashbang.anim.Movie;

public interface MovieConf
{
    public static final String DEFAULT_ANIMATION = "default";

    Movie build ();
}
