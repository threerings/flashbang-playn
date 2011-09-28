//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

public interface AnimConf
{
    Map<KeyframeType, ? extends KeyframeConf> keyframes ();

    /** Returns the number of frames in the animation.*/
    int frames();
}
