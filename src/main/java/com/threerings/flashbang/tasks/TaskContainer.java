//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.tasks;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.ObjectTask;

public abstract class TaskContainer extends ObjectTask
{
    public TaskContainer (Type type, ObjectTask... subtasks)
    {
        _type = type;

        for (ObjectTask task : subtasks) {
            addTask(task);
        }
    }

    /** Adds a child task to the TaskContainer. */
    public void addTask (ObjectTask ...tasks)
    {
        for (ObjectTask task : tasks) {
            if (_obj != null) {
                task.init(_obj);
            } // else: we'll init these tasks when we're added to an object
            _tasks.add(task);
            _completedTasks.add(null);
            _activeTaskCount += 1;
        }
    }

    /** Removes all tasks from the TaskContainer. */
    public void removeAllTasks ()
    {
        _invalidated = true;
        _tasks.clear();
        _completedTasks.clear();
        _activeTaskCount = 0;
    }

    /** Returns true if the TaskContainer has any child tasks. */
    public boolean hasTasks ()
    {
        return (_activeTaskCount > 0);
    }

    @Override
    public void init (GameObject obj)
    {
        _obj = obj;
        for (int ii = 0, ll = _tasks.size(); ii < ll; ii++) {
            ObjectTask task = _tasks.get(ii);
            if (task != null) {
                task.init(obj);
            }
        }
    }

    @Override
    public boolean update (float dt)
    {
        _invalidated = false;

        int n = _tasks.size();
        for (int ii = 0; ii < n; ++ii) {
            ObjectTask task = _tasks.get(ii);

            // we can have holes in the array
            if (null == task) {
                continue;
            }

            boolean complete = task.update(dt);

            if (_invalidated) {
                // The TaskContainer was destroyed by its containing
                // GameObject during task iteration. Stop processing immediately.
                return false;
            }

            if (!complete && Type.PARALLEL != _type) {
                // Serial and Repeating tasks proceed one task at a time
                break;

            } else if (complete) {
                // the task is complete - move it the completed tasks array
                _completedTasks.set(ii, _tasks.get(ii));
                _tasks.set(ii, null);
                _activeTaskCount -= 1;
            }
        }

        // if this is a repeating task and all its tasks have been completed, start over again
        if (_type == Type.REPEATING && 0 == _activeTaskCount && !_completedTasks.isEmpty()) {
            List<ObjectTask> completedTasks = _completedTasks;

            _tasks = Lists.newArrayList();
            _completedTasks = Lists.newArrayList();

            for (ObjectTask completedTask : completedTasks) {
                addTask(completedTask.clone());
            }
        }

        // once we have no more active tasks, we're complete
        return (0 == _activeTaskCount);
    }

    @Override
    public ObjectTask clone ()
    {
        TaskContainer theClone = createClone();
        initClone(theClone);
        return theClone;
    }

    protected abstract TaskContainer createClone ();

    protected void initClone (TaskContainer theClone)
    {
        theClone._obj = _obj;
        theClone._tasks = cloneSubtasks();
        theClone._activeTaskCount = theClone._tasks.size();
        theClone._completedTasks = Lists.newArrayListWithExpectedSize(theClone._tasks.size());
        for (int ii = 0; ii < theClone._tasks.size(); ++ii) {
            theClone._completedTasks.add(null);
        }
    }

    protected List<ObjectTask> cloneSubtasks ()
    {
        Preconditions.checkState(_tasks.size() == _completedTasks.size());

        List<ObjectTask> out = Lists.newArrayListWithExpectedSize(_tasks.size());

        // clone each child task and put it in the cloned container
        for (int ii = 0; ii < _tasks.size(); ++ii) {
            ObjectTask task = (null != _tasks.get(ii)) ?
                _tasks.get(ii) : _completedTasks.get(ii);
            Preconditions.checkNotNull(task);
            out.add(task.clone());
        }

        return out;
    }

    protected enum Type {
        PARALLEL,
        SERIAL,
        REPEATING;
    };

    protected final Type _type;
    protected GameObject _obj;
    protected List<ObjectTask> _tasks = Lists.newArrayList();
    protected List<ObjectTask> _completedTasks = Lists.newArrayList();
    protected int _activeTaskCount;
    protected boolean _invalidated;
}
