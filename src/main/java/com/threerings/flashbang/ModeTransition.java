//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

public enum ModeTransition {
    PUSH(true),
    UNWIND(false),
    INSERT(true),
    REMOVE(false),
    CHANGE(true);

    public final boolean requiresMode;

    ModeTransition (boolean requiresMode) {
        this.requiresMode = requiresMode;
    }
}
