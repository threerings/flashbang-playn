//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import java.util.Map;

import playn.core.Json;

import react.RMap;

import tripleplay.util.JsonUtil;

public class EditableAnimConf implements AnimConf
{
    public final RMap<KeyframeType, EditableKeyframeConf> keyframes = RMap.create();

    public EditableAnimConf () {
        for (KeyframeType kt : KeyframeType.values()) {
            keyframes.put(kt,
                new EditableKeyframeConf(0, kt.defaultValue, InterpolatorType.LINEAR, null));
        }
    }

    public EditableAnimConf (Json.Object obj) {
        this();// Fill in the defaults. They'll be repaced if there's a frame 0 keyframe
        Json.Object keyframes = obj.getObject("keyframes");
        for (String type : keyframes.getKeys()) {
            KeyframeType kt = KeyframeType.valueOf(type);
            for (Json.Object kfObj : keyframes.getArray(type, Json.Object.class)) {
                add(kt, kfObj.getInt("frame"), (float)kfObj.getNumber("value"),
                    JsonUtil.getEnum(kfObj, "interp", InterpolatorType.class,
                        InterpolatorType.LINEAR));
            }
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

    public boolean hasKeyframe (int frame) {
        for (KeyframeType kt : KeyframeType.values()) {
            EditableKeyframeConf kf = keyframes.get(kt);
            while (kf.next() != null && kf.frame() < frame) { kf = kf.next.get(); }
            if (kf.frame() == frame) return true;
        }
        return false;
    }

    public void add (KeyframeType kt, int frame, float value, InterpolatorType interp) {
        EditableKeyframeConf kf = keyframes.get(kt);
        while (kf.next() != null && kf.frame() < frame) { kf = kf.next.get(); }
        if (kf.frame() != frame) {
            kf.next.update(new EditableKeyframeConf(frame, value, interp, kf.next.get()));
        } else {
            kf.value.update(value);
            kf.interpolator.update(interp);
        }
    }

    public void write (Json.Writer writer) {
        writer.object().key("keyframes").object();
        for (Map.Entry<KeyframeType, EditableKeyframeConf> entry : keyframes.entrySet()) {
            EditableKeyframeConf kf = entry.getValue();
            KeyframeType kt = entry.getKey();
            if (kf.next() == null && kf.value() == kt.defaultValue && kf.frame() == 0) continue;
            writer.key(kt.name()).array();
            for (; kf != null; kf = kf.next.get()) {
                writer.object().
                    key("frame").value(kf.frame()).
                    key("value").value(kf.value()).
                    key("interp").value(kf.interpolator.get().name()).endObject();
            }
            writer.endArray();
        }
        writer.endObject().endObject();
    }

    @Override public String toString () {
        return "EditableAnimConf [keyframes=" + keyframes + "]";
    }
}
