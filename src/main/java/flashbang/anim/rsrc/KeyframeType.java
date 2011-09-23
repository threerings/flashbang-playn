//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

public enum KeyframeType
{
    X_LOCATION("X", 0), Y_LOCATION("Y", 0), X_SCALE("X Scale", 1), Y_SCALE("Y Scale", 1),
        ROTATION("Rotation", 0), ALPHA("Alpha", 1);

    public final String displayName;
    public final float defaultValue;

    private KeyframeType (String displayName, float defaultValue) {
        this.displayName = displayName;
        this.defaultValue = defaultValue;
    }
}
