//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import playn.core.GroupLayer;
import react.ConnectionGroup;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import flashbang.components.LayerComponent;

public class GameObjectDatabase
{
    /**
     * Adds a GameObject to the GameObjectDatabase. The GameObject must not be owned by another
     * database.
     *
     * @return the GameObject's GameObjectRef
     */
    public GameObjectRef addObject (GameObject obj)
    {
        Preconditions.checkState(!obj.isZombie(), "GameObject is a zombie: %s", obj);

        // create a new GameObjectRef
        GameObjectRef ref = new GameObjectRef();
        ref._obj = obj;

        // link the ref into the list
        GameObjectRef oldListHead = _listHead;
        _listHead = ref;
        if (null != oldListHead) {
            ref._next = oldListHead;
            oldListHead._prev = ref;
        }
        ++_objectCount;

        // does the object have names?
        for (String objectName : obj.names()) {
            GameObject oldObj = _namedObjects.put(objectName, obj);
            Preconditions.checkState(oldObj == null,
                "Two objects with the same name (%s) added to the GameObjectDatabase", objectName);
        }

        // add the object to the groups it belongs to
        for (String groupName : obj.groups()) {
            List<GameObjectRef> groupList = (_groupedObjects.get(groupName));
            if (groupList == null) {
                groupList = Lists.newArrayList();
                _groupedObjects.put(groupName, groupList);
            }

            groupList.add(ref);
        }

        // initialize object
        obj.addedToDbInternal(this, ref);

        return ref;
    }

    /**
     * A convenience function that adds the given SceneObject to the mode and attaches its
     * Layer to the given parent.
     *
     * @param parentLayer the GroupLayer to attach the Layer to.
     */
    public GameObjectRef addObject (GameObject obj, GroupLayer parentLayer)
    {
        Preconditions.checkArgument(obj instanceof LayerComponent,
            "obj must implement LayerComponent");

        // Attach the object to a GroupLayer
        // (This is purely a convenience - the client is free to do the attaching themselves)
        parentLayer.add(((LayerComponent) obj).layer());

        return addObject(obj);
    }

    /**
     * Removes a GameObject from the database
     */
    public void destroyObject (GameObjectRef ref)
    {
        if (null == ref) {
            return;
        }

        GameObject obj = ref.obj();
        if (null == obj) {
            return;
        }

        // Destroy any objects that are dependent on this one. We do this here, while this
        // GameObject is still technically "live", instead of in GameObject.removedFromDbInternal,
        // to prevent dependents' destruction logic from affecting this GameObject while
        // it is mid-destruction.
        for (GameObjectRef dependentRef : obj._dependentObjectRefs) {
            destroyObject(dependentRef);
        }

        // the ref no longer points to the object
        ref._obj = null;

        // remove objectName mappings
        for (String objectName : obj.names()) {
            _namedObjects.remove(objectName);
        }

        // objectGroup removal and objectList unlinking takes place in finalizeObjectRemoval()

        --_objectCount;

        obj.removedFromDbInternal();
        obj.cleanupInternal();

        if (_objectsPendingRemoval == null) {
            _objectsPendingRemoval = Lists.newArrayList();
        }
        _objectsPendingRemoval.add(obj);
    }

    /**
     * @return the object with the given name, or null if no such object exists in the database.
     */
    public GameObject getObject (String name)
    {
        return _namedObjects.get(name);
    }

    /**
     * @return the objects in the given group.
     */
    public Iterable<GameObject> getObjects (String groupName)
    {
        List<GameObjectRef> refs = _groupedObjects.get(groupName);
        if (refs == null || refs.isEmpty()) {
            return Collections.emptyList();
        }

        return Iterables.filter(Iterables.transform(refs, GameObjectRef.TO_OBJECT),
            Predicates.notNull());
    }

    /**
     * @return the number of objects in the given group
     */
    public int countObjects (String groupName)
    {
        List<GameObjectRef> refs = _groupedObjects.get(groupName);
        if (refs == null) {
            return 0;
        } else {
            int count = 0;
            for (GameObjectRef ref : refs) {
                if (ref.isLive()) {
                    ++count;
                }
            }
            return count;
        }
    }

    /**
     * Guarantees that the <code>second</code> GameObject will have its update logic run after
     * <code>first</code> during the update loop.
     */
    public void setUpdateOrder (GameObject first, GameObject second)
    {
        Preconditions.checkState(second._parentDb == this && first._parentDb == this,
            "GameObject doesn't belong to this GameObjectDatabase");
        Preconditions.checkState(second.isLiveObject() && first.isLiveObject(),
            "GameObject is not live");

        // unlink second from the list
        unlink(second);

        // relink it directly after first
        GameObjectRef firstRef = first._ref;
        GameObjectRef secondRef = second._ref;
        GameObjectRef nextRef = firstRef._next;

        firstRef._next = secondRef;
        secondRef._prev = firstRef;
        secondRef._next = nextRef;
        if (nextRef != null) {
            nextRef._prev = secondRef;
        }
    }

    /** Returns the number of live GameObjects in this ObjectDB. */
    public int objectCount ()
    {
        return _objectCount;
    }

    /**
     * Returns the number of seconds this ObjectDB has been running, as measured by calls to
     * update().
     */
    public float runningTime ()
    {
        return _runningTime;
    }

    /** Called once per update tick. Updates all objects in the mode. */
    protected void update (float dt)
    {
        beginUpdate(dt);
        endUpdate(dt);
        _runningTime += dt;
    }

    /** Updates all objects in the mode. */
    protected void beginUpdate (float dt)
    {
        // update all objects
        GameObjectRef ref = _listHead;
        while (null != ref) {
            if (null != ref._obj) {
                ref._obj.updateInternal(dt);
            }
            ref = ref._next;
        }
    }

    /** Removes dead objects from the object list at the end of an update. */
    protected void endUpdate (float dt)
    {
        // clean out all objects that were destroyed during the update loop
        if (_objectsPendingRemoval != null) {
            for (GameObject obj : _objectsPendingRemoval) {
                finalizeObjectRemoval(obj);
            }
            _objectsPendingRemoval = null;
        }
    }

    /** Removes a single dead object from the object list. */
    protected void finalizeObjectRemoval (GameObject obj)
    {
        Preconditions.checkState(null != obj._ref && null == obj._ref._obj);

        // unlink the object ref
        unlink(obj);

        // remove the object from the groups it belongs to
        // (we do this here, rather than in destroyObject(),
        // because client code might be iterating an
        // object group Array when destroyObject is called)
        GameObjectRef ref = obj._ref;
        for (String groupName : obj.groups()) {
            List<GameObjectRef> groupList = _groupedObjects.get(groupName);
            Preconditions.checkState(groupList != null, "destroyed GameObject is returning " +
                    "different object groups than it did on creation [obj=%s]", obj);

            boolean wasInList = groupList.remove(ref);
            Preconditions.checkState(wasInList, "destroyed GameObject is returning " +
                "different object groups than it did on creation [obj=%s]", obj);
        }

        obj._parentDb = null;
    }

    /**
     * Destroys all GameObjects contained by this GameObjectDatabase. Applications generally
     * don't need to call this function - it's called automatically when an {@link AppMode} is
     * popped from the mode stack.
     */
    protected void shutdown ()
    {
        Preconditions.checkState(!_hasShutdown, "GameObjectDatabase has already been shut down");
        _hasShutdown = true;

        GameObjectRef ref = _listHead;
        while (null != ref) {
            if (ref.isLive()) {
                GameObject obj = ref._obj;
                ref._obj = null;
                obj.cleanupInternal();
            }
            ref = ref._next;
        }

        _listHead = null;
        _objectCount = 0;
        _objectsPendingRemoval = null;
        _namedObjects = null;
        _groupedObjects = null;
        _conns.disconnect();
        _conns = null;
    }

    protected void unlink (GameObject obj)
    {
        GameObjectRef ref = obj.ref();

        GameObjectRef prev = ref._prev;
        GameObjectRef next = ref._next;

        if (null != prev) {
            prev._next = next;
        } else {
            // if prev is null, ref was the head of the list
            Preconditions.checkState(ref == _listHead);
            _listHead = next;
        }

        if (null != next) {
            next._prev = prev;
        }
    }

    protected float _runningTime;
    protected boolean _hasShutdown;

    protected GameObjectRef _listHead;
    protected int _objectCount;

    protected List<GameObject> _objectsPendingRemoval;

    protected Map<String, GameObject> _namedObjects = Maps.newHashMap();
    protected Map<String, List<GameObjectRef>> _groupedObjects = Maps.newHashMap();

    protected ConnectionGroup _conns = new ConnectionGroup();
}
