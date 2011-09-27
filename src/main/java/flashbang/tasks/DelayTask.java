//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import flashbang.ObjectTask;

public class DelayTask extends TimedTask
{
    public DelayTask (float time)
    {
        super(time);
    }

    @Override
    public ObjectTask clone ()
    {
        return new DelayTask(_totalTime);
    }
}
