//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import react.ConnectionGroup;
import react.UnitSignal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.threerings.flashbang.tasks.ParallelTask;
import com.threerings.flashbang.tasks.TaskContainer;

public class GameObject
{
    public final UnitSignal destroyed = new UnitSignal();

    /**
     * Returns the unique GameObjectRef that stores a reference to this GameObject.
     */
    public final GameObjectRef ref ()
    {
        return _ref;
    }

    /**
     * Returns the GameObjectDatabase that this object is contained in.
     */
    public final GameObjectDatabase db ()
    {
        return _parentDb;
    }

    /**
     * A "zombie" GameObject was previously added to a GameObjectDatabase and subsequently
     * destroyed. In general, you shouldn't use zombie objects.
     */
    public final boolean isZombie ()
    {
        return (_ref != null && _parentDb == null);
    }

    /**
     * Returns true if the object is in an ObjectDB and is "live"
     * (not pending removal from the database)
     */
    public final boolean isLiveObject ()
    {
        return (_ref != null && _ref.isLive());
    }

    /** Removes the GameObject from its parent database. */
    public void destroySelf ()
    {
        _parentDb.destroyObject(_ref);
    }

    /** Adds an unnamed task to this GameObject. */
    public void addTask (ObjectTask task)
    {
        if (_lazyAnonymousTasks == null) {
            _lazyAnonymousTasks = new ParallelTask();
        }
        _lazyAnonymousTasks.addTask(task);
    }

    /** Adds a named task to this GameObject. */
    public void addTask (String name, ObjectTask task)
    {
        addTask(name, task, false);
    }

    /**
     * Adds a named task to this GameObject.
     * @param removeExistingTasksWithName if true, all tasks with the given name are removed
     * from the GameObject first
     */
    public void addTask (String name, ObjectTask task, boolean removeExistingTasksWithName)
    {
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");
        TaskContainer tasks = findNamedTaskContainer(name, true);

        if (removeExistingTasksWithName) {
            tasks.removeAllTasks();
        }
        tasks.addTask(task);
    }

    /** Removes all tasks from the GameObject. */
    public void removeAllTasks ()
    {
        if (_updatingTasks) {
            if (_lazyAnonymousTasks != null) {
                _lazyAnonymousTasks.removeAllTasks();
            }
            for (ParallelTask container : _namedTasks.values()) {
                container.removeAllTasks();
            }
        } else {
            _lazyAnonymousTasks = null;
            _namedTasks.clear();
        }
    }

    /** Removes all tasks with the given name from the GameObject. */
    public void removeNamedTasks (String name)
    {
        Preconditions.checkArgument(!name.isEmpty(), "name cannot be empty");

        if (!_updatingTasks) {
            _namedTasks.remove(name);
        } else {
            TaskContainer tasks = findNamedTaskContainer(name, false);
            if (tasks != null) {
                tasks.removeAllTasks();
            }
        }
    }

    /** Returns true if the GameObject has any tasks. */
    public boolean hasTasks ()
    {
        if (_lazyAnonymousTasks != null && _lazyAnonymousTasks.hasTasks()) {
            return true;

        } else {
            for (ParallelTask container : _namedTasks.values()) {
                if (container.hasTasks()) {
                    return true;
                }
            }
            if (_pendingNamedTasks != null) {
                for (ParallelTask container : _pendingNamedTasks.values()) {
                    if (container.hasTasks()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /** Returns true if the GameObject has any tasks with the given name. */
    public boolean hasTasks (String name)
    {
        TaskContainer namedTask = findNamedTaskContainer(name, false);
        return (namedTask != null && namedTask.hasTasks());
    }

    /**
     * Returns this object's names. (Objects can have multiple names.)
     * Two objects in the same mode cannot have the same name.
     * Objects cannot change their names once added to a mode.
     * <code>
     * @Override
     * public Iterable<String> names ()
     * {
     *     return concat("MyName", super.names());
     * }
     * </code>
     */
    public Iterable<String> names ()
    {
        return Collections.emptyList();
    }

    /**
     * Override to return the groups that this object belongs to. E.g.:
     * <code>
     * @Override
     * public Iterable<String> groups ()
     * {
     *     return concat("FooGroup", super.groups());
     * }
     * </code>
     */
    public Iterable<String> groups ()
    {
        return Collections.emptyList();
    }

    /**
     * Causes the lifecycle of the given GameObject to be managed by this object. Dependent
     * objects will be added to this object's GameObjectDatabase, and will be destroyed when this
     * object is destroyed.
     */
    public void addDependentObject (GameObject obj)
    {
        Preconditions.checkState(!isZombie(), "GameObject is a zombie: %s", this);

        if (_parentDb != null) {
            manageDependentObject(obj);
        } else {
            // If this GameObject isn't part of a database, add the new dependent
            // to a list. It'll be added in this GameObject's addedToDbInternal
            _pendingDependentObjects.add(obj);
        }
    }

    /**
     * Called once per update tick. (Subclasses can override this to do something useful.)
     *
     * @param dt the number of seconds that have elapsed since the last update.
     */
    protected void update (float dt)
    {
    }

    /**
     * Called immediately after the GameObject has been added to a GameObjectDatabase.
     * (Subclasses can override this to do something useful.)
     */
    protected void addedToDb ()
    {
    }

    /**
     * Called immediately after the GameObject has been removed from a GameObjectDatabase.
     *
     * removedFromDb is not called when the GameObject's AppMode is removed from the mode stack.
     * For logic that must be run in this instance, see {@link #cleanup}.
     *
     * (Subclasses can override this to do something useful.)
     */
    protected void removedFromDb ()
    {
    }

    /**
     * Called after the GameObject has been removed from the active AppMode, or if the
     * object's containing AppMode is removed from the mode stack.
     *
     * If the GameObject is removed from the active AppMode, {@link #removedFromDb}
     * will be called before destroyed.
     *
     * {@link #cleanup} should be used for logic that must be always be run when the GameObject is
     * destroyed (disconnecting event listeners, releasing resources, etc).
     *
     * (Subclasses can override this to do something useful.)
     */
    protected void cleanup ()
    {
    }

    protected void manageDependentObject (GameObject obj)
    {
        GameObjectRef ref;

        // the dependent object may already be in the DB
        if (obj._parentDb != null) {
            Preconditions.checkState(obj._parentDb == _parentDb,
                "Dependent object belongs to another GameObjectDatabase");
            ref = obj.ref();
        } else {
            ref = _parentDb.addObject(obj);
        }

        _dependentObjectRefs.add(ref);
    }

    protected ParallelTask findNamedTaskContainer (String name, boolean create)
    {
        ParallelTask task = _namedTasks.get(name);
        if (task == null && _pendingNamedTasks != null) {
            task = _pendingNamedTasks.get(name);
        }

        if (task == null && create) {
            task = new ParallelTask();

            // If we're updating tasks, we can't modify namedTasks, or we'll break task
            // iteration. Store our new task in a map of pending tasks.
            if (_updatingTasks) {
                if (_pendingNamedTasks != null) {
                    _pendingNamedTasks = Maps.newHashMap();
                }
                _pendingNamedTasks.put(name, task);
            } else {
                _namedTasks.put(name, task);
            }
        }

        return task;
    }

    void addedToDbInternal (GameObjectDatabase parentDb, GameObjectRef ref)
    {
        _parentDb = parentDb;
        _ref = ref;

        // Add dependent objects to the database
        for (GameObject dep : _pendingDependentObjects) {
            manageDependentObject(dep);
        }
        _pendingDependentObjects = null;

        addedToDb();
    }

    void removedFromDbInternal ()
    {
        // dependent objects are destroyed by GameObjectDatabase.destroyObject,
        // to prevent situations where logic that runs during dependents' destruction
        // interferes with this object during its own destruction
        removedFromDb();
        this.destroyed.emit();
    }

    void cleanupInternal ()
    {
        _conns.disconnect();

        // null these out so that further attempts to use them immediately NPE
        _dependentObjectRefs = null;
        _pendingDependentObjects = null;
        _conns = null;
        _parentDb = null;

        // We deliberately don't null out our _ref, for the isZombie() check

        cleanup();
    }

    void updateInternal (float dt)
    {
        if (_lazyAnonymousTasks != null || !_namedTasks.isEmpty()) {
            _updatingTasks = true;

            // Anonymous tasks
            if (_lazyAnonymousTasks != null) {
                if (_lazyAnonymousTasks.update(dt, this)) {
                    _lazyAnonymousTasks = null;
                }
            }

            // Named tasks
            for (ParallelTask namedTask : _namedTasks.values()) {
                namedTask.update(dt, this);
            }

            if (_pendingNamedTasks != null) {
                _namedTasks.putAll(_pendingNamedTasks);
                _pendingNamedTasks = null;
            }

            _updatingTasks = false;
        }

        // Call update() if we're still alive (a task could've destroyed us)
        if (isLiveObject()) {
            update(dt);
        }
    }

    /**
     * Helper function for implementing getGroups() and getNames() overrides.
     */
    protected static <T> Iterable<T> concat (T obj, Iterable<T> other)
    {
        return Iterables.concat(Collections.singleton(obj), other);
    }

    protected static <T> Iterable<T> concat (T obj1, T obj2, Iterable<T> other)
    {
        return Iterables.concat(Collections.singleton(obj1), concat(obj2, other));
    }

    // Note: this is null until needed. Subclassers beware
    protected ParallelTask _lazyAnonymousTasks;
    // Use a LinkedHashMap so that iteration order is guaranteed
    protected Map<String, ParallelTask> _namedTasks = Maps.newLinkedHashMap();
    // Named task containers that were created while _updatingTasks was true
    protected Map<String, ParallelTask> _pendingNamedTasks;
    protected boolean _updatingTasks;

    protected ConnectionGroup _conns = new ConnectionGroup();
    protected List<GameObjectRef> _dependentObjectRefs = Lists.newArrayList();
    protected List<GameObject> _pendingDependentObjects = Lists.newArrayList();

    GameObjectRef _ref;
    GameObjectDatabase _parentDb;
}
