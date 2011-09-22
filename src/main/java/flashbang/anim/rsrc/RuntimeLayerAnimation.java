//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

public class RuntimeLayerAnimation implements LayerAnimation
{
    public RuntimeLayerAnimation (String layerSelector, Map<KeyframeType, Keyframe> keyframes,
        int frames) {
        _layerSelector = layerSelector;
        _keyframes = keyframes;
        _frames = frames;
    }

    @Override public String layerSelector () { return _layerSelector; }
    @Override public Map<KeyframeType, Keyframe> keyframes () { return _keyframes; }
    @Override public int frames () { return _frames; }

    private final int _frames;
    private final String _layerSelector;
    private final Map<KeyframeType, Keyframe> _keyframes;
}
