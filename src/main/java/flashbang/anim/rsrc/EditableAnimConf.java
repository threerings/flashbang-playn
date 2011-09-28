//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import react.RMap;
import react.Value;

public class EditableAnimConf implements AnimConf
{
    public final RMap<KeyframeType, EditableKeyframeConf> keyframes = RMap.create();

    public EditableAnimConf () {
        for (KeyframeType kt : KeyframeType.values()) {
            keyframes.put(kt, new EditableKeyframeConf(0, kt.defaultValue, null));
        }
    }

    @Override public Map<KeyframeType, EditableKeyframeConf> keyframes () { return keyframes; }

    @Override public int frames () {
        int max = 1;
        for (KeyframeConf kf : keyframes.values()) {
            while (kf.next() != null) { kf = kf.next(); }
            max = Math.max(kf.frame(), max);
        }
        return max;
    }

    public void add (KeyframeType kt, int frame, float value) {
        EditableKeyframeConf kf = keyframes.get(kt);
        while (kf.next() != null && kf.frame() < frame) { kf = kf.next.get(); }
        if (kf.frame() != frame) {
            kf.next.update(new EditableKeyframeConf(frame, value, kf.next.get()));
        } else {
            kf.value.update(value);
        }
    }

    @Override public String toString () {
        return "EditableAnimConf [keyframes=" + keyframes + "]";
    }
}
