//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import react.Value;

import tripleplay.util.Interpolator;

public class EditableKeyframe implements Keyframe
{
    public final Value<Integer> frame;
    public final Value<Float> value;
    public final Value<EditableKeyframe> next;

    public EditableKeyframe (int frame, float value, EditableKeyframe next) {
        this.value = Value.create(value);
        this.frame = Value.create(frame);
        this.next = Value.create(next);
    }

    @Override public int frame () { return frame.get(); }
    @Override public float value () { return value.get(); }
    @Override public Interpolator interp () { return Interpolator.LINEAR; }
    @Override public Keyframe next () { return next.get(); }
}
