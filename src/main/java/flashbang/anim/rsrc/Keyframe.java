//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import tripleplay.util.Interpolator;

public interface Keyframe
{
    int frame();
    float value();
    Interpolator interp();
    Keyframe next();
}
