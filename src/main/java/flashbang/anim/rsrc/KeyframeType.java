//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

public enum KeyframeType
{
    X_LOCATION(0), Y_LOCATION(0), X_SCALE(1), Y_SCALE(1), ROTATION(0), ALPHA(1);

    public final float defaultValue;

    private KeyframeType (float defaultValue) {
        this.defaultValue = defaultValue;
    }
}
