//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang;

import com.google.common.base.Preconditions;

import flashbang.rsrc.ResourceManager;

public class Flashbang
{
    public static FlashbangApp app ()
    {
        return _app;
    }

    public static ResourceManager rsrcs ()
    {
        return _rsrcs;
    }

    /**
     * @return the currently-active GameObjectDatabase (or null if no GameObjectDatabase is active)
     */
    public static GameObjectDatabase objectDatabase ()
    {
        return _ctxGameObjectDatabase;
    }

    /**
     * @return the currently-active AppMode (or null if no AppMode is active)
     */
    public static AppMode mode ()
    {
        return (_ctxGameObjectDatabase instanceof AppMode ? (AppMode) _ctxGameObjectDatabase :
            null);
    }

    /**
     * @return the currently-active Viewport (the viewport that's attached to the current
     * AppMode), or null if no AppMode is active.
     */
    public static Viewport viewport ()
    {
        AppMode mode = mode();
        return (mode != null ? mode._viewport : null);
    }

    /**
     * Sets the currently-active GameObjectDatabase and runs the supplied Runnable.
     * All calls to Flashbang.objectDatabase() from within the supplied Runnable will return
     * the supplied db.
     */
    public static void withinObjectDatabase (GameObjectDatabase db, Runnable runnable)
    {
        GameObjectDatabase cur = _ctxGameObjectDatabase;
        _ctxGameObjectDatabase = db;
        try {
            runnable.run();
        } finally {
            _ctxGameObjectDatabase = cur;
        }
    }

    static void registerApp (FlashbangApp app)
    {
        Preconditions.checkState(_app == null, "A FlashbangApp already exists");
        _app = app;
    }

    protected static FlashbangApp _app;
    protected static ResourceManager _rsrcs = new ResourceManager();

    protected static GameObjectDatabase _ctxGameObjectDatabase;
}
