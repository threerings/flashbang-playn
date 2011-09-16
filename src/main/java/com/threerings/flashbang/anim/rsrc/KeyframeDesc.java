//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.rsrc;

import com.google.common.base.Preconditions;

import playn.core.Json;

import tripleplay.util.Interpolator;
import tripleplay.util.JsonUtil;

import com.threerings.flashbang.desc.DataDesc;

public class KeyframeDesc
    implements DataDesc
{
    public static enum Type {
        LOCATION,
        SCALE,
        ROTATION,
        VISIBLE,
        ALPHA;

        public final int mask;

        Type () {
            Preconditions.checkState(ordinal() < Integer.SIZE);
            this.mask = 1 << ordinal();
        }
    }

    public int frameIdx;
    public Interpolator interp;
    public int typeFlags;

    public float x = 0;
    public float y = 0;
    public float scaleX = 1;
    public float scaleY = 1;
    public float rotation = 0;
    public boolean visible = true;
    public float alpha = 1;

    public void fromJson (Json.Object json)
    {
        frameIdx = JsonUtil.requireInt(json, "frameIdx");
        interp = JsonUtil.requireEnum(json, "interp", InterpolatorType.class).interp;
        typeFlags = 0;

        if (json.containsKey("x")) {
            x = JsonUtil.requireFloat(json, "x");
            y = JsonUtil.requireFloat(json, "y");
            typeFlags |= Type.LOCATION.mask;
        }

        if (json.containsKey("scaleX")) {
            scaleX = JsonUtil.requireFloat(json, "scaleX");
            scaleY = JsonUtil.requireFloat(json, "scaleY");
            typeFlags |= Type.SCALE.mask;
        }

        if (json.containsKey("rotation")) {
            rotation = JsonUtil.requireFloat(json, "rotation");
            typeFlags |= Type.ROTATION.mask;
        }

        if (json.containsKey("visible")) {
            visible = JsonUtil.requireBoolean(json, "visible");
            typeFlags |= Type.VISIBLE.mask;
        }

        if (json.containsKey("alpha")) {
            alpha = JsonUtil.requireFloat(json, "alpha");
            typeFlags |= Type.ALPHA.mask;
        }
    }

    public boolean isType (Type type)
    {
        return (typeFlags & type.mask) != 0;
    }

    public void init (KeyframeDesc prev, KeyframeDesc next)
    {
        _next = next;
        if (prev != null) {
            // init our missing values
            if (!isType(Type.LOCATION)) {
                x = prev.x;
                y = prev.y;
            }
            if (!isType(Type.SCALE)) {
                scaleX = prev.scaleX;
                scaleY = prev.scaleY;
            }
            if (!isType(Type.ROTATION)) {
                rotation = prev.rotation;
            }
            if (!isType(Type.VISIBLE)) {
                visible = prev.visible;
            }
            if (!isType(Type.ALPHA)) {
                alpha = prev.alpha;
            }
        }
    }

    public KeyframeDesc next ()
    {
        return _next;
    }

    public boolean validForFrame (int idx)
    {
        return (idx >= frameIdx && idx <= endFrameIdx());
    }

    public int endFrameIdx ()
    {
        return (_next != null ? _next.frameIdx - 1 : Integer.MAX_VALUE);
    }

    protected KeyframeDesc _next;
}
