//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

public interface ObjectTask extends Cloneable
{
    /**
     * Updates the ObjectTask.
     * @return true if the task has completed, otherwise false.
     */
    boolean update (float dt, GameObject obj);

    /**
     * @return a clone of the ObjectTask
     */
    ObjectTask clone ();
}
