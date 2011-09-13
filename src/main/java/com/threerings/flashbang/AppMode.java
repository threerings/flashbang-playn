//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import tripleplay.util.Input;

import com.google.common.base.Preconditions;

import com.threerings.flashbang.components.LayerComponent;

public class AppMode extends GameObjectDatabase
{
    public final Input input = new Input();
    public final GroupLayer modeLayer = PlayN.graphics().createGroupLayer();

    /**
     * @return the currently-active AppMode (or null if no AppMode is active)
     */
    public static AppMode get ()
    {
        GameObjectDatabase cur = GameObjectDatabase.get();
        return (cur instanceof AppMode ? (AppMode) cur : null);
    }

    /** Returns the AppMode's FlashbangApp */
    public final FlashbangApp app ()
    {
        return _app;
    }

    /** Returns the Viewport that this AppMode lives in */
    public final Viewport viewport()
    {
        return _viewport;
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

    /** Called when the mode is added to the mode stack */
    protected void setup ()
    {
    }

    /** Called when the mode is removed from the mode stack */
    protected void destroy ()
    {
    }

    /** Called when the mode becomes active on the mode stack */
    protected void enter ()
    {
    }

    /** Called when the mode becomes inactive on the mode stack */
    protected void exit()
    {
    }

    /**
     * Returns the <code>Pointer.Listener</code> that responds to pointer events in this
     * AppMode.
     */
    protected Pointer.Listener pointerListener ()
    {
        return _plistener;
    }

    /**
     * Assigns a new <code>Pointer.Listener</code> to respond to pointer events in this
     * AppMode.
     */
    protected void setPointerListener (Pointer.Listener listener)
    {
        Preconditions.checkNotNull(listener);
        _plistener = listener;
    }

    void setupInternal (FlashbangApp app, Viewport viewport)
    {
        _app = app;
        _viewport = viewport;
        setup();
    }

    void destroyInternal ()
    {
        destroy();
        shutdown();
        _app = null;
        _viewport = null;
        modeLayer.destroy();
    }

    void enterInternal ()
    {
        Preconditions.checkState(!_active);
        _active = true;
        enter();
    }

    void exitInternal ()
    {
        Preconditions.checkState(_active);
        _active = false;
        exit();
    }


    protected FlashbangApp _app;
    protected Viewport _viewport;

    boolean _active;

    private Pointer.Listener _plistener = input.plistener;
}
