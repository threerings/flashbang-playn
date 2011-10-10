//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim;

import com.google.common.base.Preconditions;

import flashbang.GameObject;
import flashbang.ObjectTask;

public class PlayAnimTask extends ObjectTask
{
    public static enum Complete {
        ANIM_FINISHED,
        IMMEDIATE
    }

    public PlayAnimTask (String name, Complete complete)
    {
        _name = name;
        _complete = complete;
    }

    public PlayAnimTask (String name)
    {
        this(name, Complete.ANIM_FINISHED);
    }

    @Override public void init (GameObject target)
    {
        Preconditions.checkArgument(target instanceof Movie,
            "PlayAnimTask must be applied to Movie");
        _obj = (Movie) target;
    }

    @Override public boolean update (float dt)
    {
        if (!_started) {
            _started = true;
            _obj.play(_name);
        }

        return (_complete == Complete.IMMEDIATE ? true: _obj.frame() == _obj.frames() - 1);
    }

    @Override public ObjectTask clone ()
    {
        return new PlayAnimTask(_name, _complete);
    }

    protected final String _name;
    protected final Complete _complete;
    protected Movie _obj;
    protected boolean _started;
}
