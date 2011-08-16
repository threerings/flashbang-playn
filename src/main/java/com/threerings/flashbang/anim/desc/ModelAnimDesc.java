//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.List;

/**
 * ModelAnimDesc describes how to animate multiple layers in a single model
 */
public class ModelAnimDesc
{
    public enum EndBehavior {
        STOP,
        LOOP;
    }

    public List<LayerAnimDesc> layerAnims;
    public EndBehavior endBehavior;
    public float framerate;

    /**
     * @return The total number of frames in this animation
     */
    public int totalFrames ()
    {
        return _totalFrames;
    }

    public void init ()
    {
        _totalFrames = 0;
        for (LayerAnimDesc layerAnim : layerAnims) {
            layerAnim.init();
            _totalFrames = Math.max(_totalFrames, layerAnim.numFrames());
        }
    }

    protected int _totalFrames;
}
