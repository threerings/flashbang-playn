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
    public static enum Completion {
        ANIM_COMPLETE,
        IMMEDIATELY
    }

    public PlayAnimTask (String name, Completion completion)
    {
        _name = name;
        _completion = completion;
    }

    public PlayAnimTask (String name)
    {
        this(name, Completion.ANIM_COMPLETE);
    }

    @Override
    public void init (GameObject target)
    {
        Preconditions.checkArgument(target instanceof Movie,
            "PlayAnimTask must be applied to Movie");
        _obj = (Movie) target;
    }

    @Override
    public boolean update (float dt)
    {
        if (_animator == null) {
            _animator = _obj.play(_name);
            Preconditions.checkNotNull(_animator, "No such animation [name=%s]", _name);
        }

        // Complete when we get to the last frame
        return _animator.frame() == _animator.frames() - 1;
    }

    @Override
    public ObjectTask clone ()
    {
        return new PlayAnimTask(_name, _completion);
    }

    protected final String _name;
    protected final Completion _completion;
    protected Movie _obj;
    protected AnimationController _animator;
}
