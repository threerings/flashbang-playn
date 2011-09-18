//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import tripleplay.util.Interpolator;

public enum InterpolatorType {
    LINEAR(Interpolator.LINEAR),
    EASE_IN(Interpolator.EASE_IN),
    EASE_OUT(Interpolator.EASE_OUT),
    EASE_INOUT(Interpolator.EASE_INOUT);

    public final Interpolator interp;

    InterpolatorType (Interpolator interp) {
        this.interp = interp;
    }
}
