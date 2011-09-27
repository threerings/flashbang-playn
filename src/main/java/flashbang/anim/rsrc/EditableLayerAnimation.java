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
    public final RMap<KeyframeType, EditableKeyframe> keyframes = RMap.create();

    public EditableLayerAnimation () {
        for (KeyframeType kt : KeyframeType.values()) {
            keyframes.put(kt, new EditableKeyframe(0, kt.defaultValue, null));
        }
    }

    @Override public Map<KeyframeType, EditableKeyframe> keyframes () { return keyframes; }

    @Override public int frames () {
        int max = 1;
        for (Keyframe kf : keyframes.values()) {
            while (kf.next() != null) { kf = kf.next(); }
            max = Math.max(kf.frame(), max);
        }
        return max;
    }

    public void add (KeyframeType kt, int frame, float value) {
        EditableKeyframe kf = keyframes.get(kt);
        while (kf.next() != null && kf.frame() < frame) { kf = kf.next.get(); }
        if (kf.frame() != frame) {
            kf.next.update(new EditableKeyframe(frame, value, kf.next.get()));
        } else {
            kf.value.update(value);
        }
    }

    @Override public String toString () {
        return "EditableLayerAnimation [keyframes=" + keyframes + "]";
    }
}
