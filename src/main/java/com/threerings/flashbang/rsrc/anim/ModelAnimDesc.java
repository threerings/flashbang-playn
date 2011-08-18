//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc.anim;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Json;
import tripleplay.util.JsonUtil;

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

    public void fromJson (Json.Object json)
    {
        layerAnims = Lists.newArrayList();
        for (Json.Object layerAnimJson : JsonUtil.getArrayObjects(json, "layerAnims")) {
            LayerAnimDesc layerAnim = new LayerAnimDesc();
            layerAnim.fromJson(layerAnimJson);
            layerAnims.add(layerAnim);
        }

        endBehavior = JsonUtil.getEnum(json, "endBehavior", EndBehavior.class);
        framerate = (float) json.getNumber("framerate");

        init();
    }

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
