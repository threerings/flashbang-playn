//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import flashbang.ObjectTask;

public class SerialTask extends TaskContainer
{
    public SerialTask (ObjectTask ...subtasks)
    {
        super(Type.SERIAL, subtasks);
    }

    @Override
    protected TaskContainer createClone ()
    {
        return new SerialTask();
    }
}
