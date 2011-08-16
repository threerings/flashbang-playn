//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.List;

/**
 * ModelAnimDesc describes how to animate multiple bones in a single model
 */
public class ModelAnimDesc
{
    public enum EndBehavior {
        STOP,
        LOOP;
    }

    public List<BoneAnimDesc> boneAnims;
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
        for (BoneAnimDesc boneAnim : boneAnims) {
            boneAnim.init();
            _totalFrames = Math.max(_totalFrames, boneAnim.numFrames());
        }
    }

    protected int _totalFrames;
}
