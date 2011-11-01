//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang;

import com.google.common.base.Preconditions;

import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Pointer;

import tripleplay.ui.Interface;
import tripleplay.util.PointerInput;

public class AppMode extends GameObjectDatabase
{
    public final PointerInput input = new PointerInput();
    public final Interface iface = new Interface(input.plistener);
    public final GroupLayer modeLayer = PlayN.graphics().createGroupLayer();

    /** Returns the AppMode's FlashbangApp */
    public final FlashbangApp app ()
    {
        return _app;
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
        return iface.plistener;
    }

    @Override protected void beginUpdate (float dt) {
        super.beginUpdate(dt);
        iface.paint(0);
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
}
