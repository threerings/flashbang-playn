//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import com.google.common.base.Preconditions;

import flashbang.GameObject;
import flashbang.ObjectTask;

/**
 * Waits for the currently-playing animation on a Movie to complete
 */
public class WaitForAnimTask extends ObjectTask
{
    @Override public void init (GameObject target)
    {
        Preconditions.checkArgument(target instanceof Movie,
            "PlayAnimTask must be applied to Movie");
        _obj = (Movie) target;
    }

    @Override public boolean update (float dt)
    {
        return (_obj.frame() == _obj.frames() - 1);
    }

    @Override public ObjectTask clone ()
    {
        return new WaitForAnimTask();
    }

    protected Movie _obj;
}
