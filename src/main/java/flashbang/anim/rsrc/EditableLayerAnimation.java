//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import react.RMap;
import react.Value;

public class EditableLayerAnimation implements LayerAnimation
{
    public final Value<String> layerSelector;
    public final RMap<KeyframeType, EditableKeyframe> keyframes = RMap.create();

    public EditableLayerAnimation (String layerSelector) {
        this.layerSelector = Value.create(layerSelector);
        for (KeyframeType kt : KeyframeType.values()) {
            keyframes.put(kt, new EditableKeyframe(kt.defaultValue, 0));
        }
    }

    @Override public String layerSelector () { return layerSelector.get(); }
    @Override public Map<KeyframeType, EditableKeyframe> keyframes () { return keyframes; }

    @Override public int frames () {
        int max = 1;
        for (Keyframe kf : keyframes.values()) {
            while (kf.next() != null) { kf = kf.next(); }
            max = Math.max(kf.frame(), max);
        }
        return max;
    }
}
