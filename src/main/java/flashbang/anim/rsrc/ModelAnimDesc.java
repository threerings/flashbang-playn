//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Json;

import tripleplay.util.JsonUtil;

import flashbang.desc.DataDesc;

/**
 * ModelAnimDesc describes how to animate multiple layers in a single model
 */
public class ModelAnimDesc
    implements DataDesc
{
    public enum EndBehavior {
        STOP,
        LOOP;
    }

    public List<LayerAnimDesc> layerAnims = Lists.newArrayList();
    public EndBehavior endBehavior;
    public float framerate;

    public void fromJson (Json.Object json)
    {
        for (Json.Object layerAnimJson : json.getObjectArray("layerAnims")) {
            LayerAnimDesc layerAnim = new LayerAnimDesc();
            layerAnim.fromJson(layerAnimJson);
            layerAnims.add(layerAnim);
        }

        endBehavior = JsonUtil.requireEnum(json, "endBehavior", EndBehavior.class);
        framerate = JsonUtil.requireFloat(json, "framerate");

        init();
    }

    /**
     * @return The total number of frames in this animation
     */
    public int totalFrames ()
    {
        return _totalFrames;
    }

    protected void init ()
    {
        _totalFrames = 0;
        for (LayerAnimDesc layerAnim : layerAnims) {
            layerAnim.init();
            _totalFrames = Math.max(_totalFrames, layerAnim.numFrames());
        }
    }

    protected int _totalFrames;
}
