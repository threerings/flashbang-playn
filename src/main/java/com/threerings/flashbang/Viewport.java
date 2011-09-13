//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import java.util.List;

import playn.core.GroupLayer;
import playn.core.PlayN;

import react.UnitSignal;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Viewport contains the AppMode stack. The topmost AppMode in the stack gets ticked on every
 * update. Don't create a Viewport directly - call FlashbangApp.createViewport.
 */
public class Viewport
{
    public static final String DEFAULT = "DefaultViewport";

    public final UnitSignal topModeChanged = new UnitSignal();
    public final UnitSignal destroyed = new UnitSignal();

    public Viewport (FlashbangApp app, String name, GroupLayer parentLayer)
    {
        _app = app;
        _name = name;
        parentLayer.add(_topLayer);
    }

    /**
     * @return the Viewport's name
     */
    public String name ()
    {
        return _name;
    }

    /**
     * Causes the Viewport to be destroyed.
     * (This won't happen immediately - it'll happen at the end of the current update loop)
     */
    public void destroy ()
    {
        _destroyed = true;
    }

    /**
     * Returns the top mode on the mode stack, or null if the stack is empty.
     */
    public AppMode topMode ()
    {
        return (_modeStack.isEmpty() ? null : _modeStack.get(_modeStack.size() - 1));
    }

    /**
     * Applies the specify mode transition to the mode stack.
     * (Mode changes take effect between game updates.)
     */
    public void doModeTransition (ModeTransition type, AppMode mode, int index)
    {
        if (type.requiresMode && mode == null) {
            throw new Error("mode must be non-null for " + type);
        }

        _pendingModeTransitionQueue.add(new PendingTransition(type, mode, index));
    }

    public void doModeTransition (ModeTransition type, AppMode mode)
    {
        doModeTransition(type, mode, 0);
    }

    public void doModeTransition (ModeTransition type)
    {
        doModeTransition(type, null, 0);
    }

    /**
     * Inserts a mode into the stack at the specified index. All modes
     * at and above the specified index will move up in the stack.
     * (Mode changes take effect between game updates.)
     *
     * @param mode the AppMode to add
     * @param index the stack position to add the mode at.
     * You can use a negative integer to specify a position relative
     * to the top of the stack (for example, -1 is the top of the stack).
     */
    public void insertMode (AppMode mode, int index)
    {
        doModeTransition(ModeTransition.INSERT, mode, index);
    }

    /**
     * Removes a mode from the stack at the specified index. All
     * modes above the specified index will move down in the stack.
     * (Mode changes take effect between game updates.)
     *
     * @param index the stack position to add the mode at.
     * You can use a negative integer to specify a position relative
     * to the top of the stack (for example, -1 is the top of the stack).
     */
    public void removeMode (int index)
    {
        doModeTransition(ModeTransition.REMOVE, null, index);
    }

    /**
     * Pops the top mode from the stack, if the modestack is not empty, and pushes
     * a new mode in its place.
     * (Mode changes take effect between game updates.)
     */
    public void changeMode (AppMode mode)
    {
        doModeTransition(ModeTransition.CHANGE, mode);
    }

    /**
     * Pushes a mode to the mode stack.
     * (Mode changes take effect between game updates.)
     */
    public void pushMode (AppMode mode)
    {
        doModeTransition(ModeTransition.PUSH, mode);
    }

    /**
     * Pops the top mode from the mode stack.
     * (Mode changes take effect between game updates.)
     */
    public void popMode ()
    {
        doModeTransition(ModeTransition.REMOVE, null, -1);
    }

    /**
     * Pops all modes from the mode stack.
     * Mode changes take effect before game updates.
     */
    public void popAllModes ()
    {
        doModeTransition(ModeTransition.UNWIND);
    }

    /**
     * Pops modes from the stack until the specified mode is reached.
     * If the specified mode is not reached, it will be pushed to the top of the mode stack.
     * Mode changes take effect before game updates.
     */
    public void unwindToMode (AppMode mode)
    {
        doModeTransition(ModeTransition.UNWIND, mode);
    }

    public void update (final float dt)
    {
        handleModeTransitions();

        // update the top mode
        if (!_modeStack.isEmpty()) {
            final AppMode topMode = topMode();
            topMode.within(new Runnable() {
                @Override public void run () {
                    topMode.update(dt);
                }
            });
        }
    }

    protected void handleModeTransitions ()
    {
        if (_pendingModeTransitionQueue.isEmpty()) {
            return;
        }

        final AppMode initialTopMode = topMode();

        // create a new _pendingModeTransitionQueue right now
        // so that we can properly handle mode transition requests
        // that occur during the processing of the current queue
        List<PendingTransition> transitionQueue = _pendingModeTransitionQueue;
        _pendingModeTransitionQueue = Lists.newArrayList();

        for (PendingTransition transition : transitionQueue) {
            switch (transition.type) {
            case PUSH:
                addModeNow(transition.mode, _modeStack.size());
                break;

            case INSERT:
                addModeNow(transition.mode, transition.index);
                break;

            case REMOVE:
                removeModeNow(transition.index);
                break;

            case CHANGE:
                // a pop followed by a push
                if (topMode() != null) {
                    removeModeNow(-1);
                }
                addModeNow(transition.mode, _modeStack.size());
                break;

            case UNWIND:
                // pop modes until we find the one we're looking for
                while (!_modeStack.isEmpty() && topMode() != transition.mode) {
                    removeModeNow(-1);
                }

                Preconditions.checkState(topMode() == transition.mode || _modeStack.isEmpty());

                if (_modeStack.isEmpty() && transition.mode != null) {
                    addModeNow(transition.mode, _modeStack.size());
                }
                break;
            }
        }

        // Ensure that our modes are rendered at their proper depths
        for (int ii = 0; ii < _modeStack.size(); ++ii) {
            _modeStack.get(ii).modeLayer.setDepth(ii);
        }

        final AppMode topMode = topMode();
        if (topMode != initialTopMode) {
            if (null != initialTopMode && initialTopMode._active) {
                initialTopMode.within(new Runnable() {
                    @Override public void run () {
                        initialTopMode.exitInternal();
                    }
                });
            }
            if (null != topMode) {
                topMode.within(new Runnable() {
                    @Override public void run () {
                        topMode.enterInternal();
                    }
                });
            }
            this.topModeChanged.emit();
        }
    }

    protected void addModeNow (final AppMode mode, int index)
    {
        Preconditions.checkNotNull(mode, "Can't insert a null mode in the mode stack");

        if (index < 0) {
            index = _modeStack.size() + index;
        }
        index = Math.max(index, 0);
        index = Math.min(index, _modeStack.size());

        _modeStack.add(index, mode);
        _topLayer.add(mode.modeLayer);

        mode.within(new Runnable() {
            @Override public void run () {
                mode.setupInternal(_app, Viewport.this);
            }
        });
    }

    protected void removeModeNow (int index)
    {
        Preconditions.checkState(!_modeStack.isEmpty(), "Can't remove a mode from an empty stack");

        if (index < 0) {
            index = _modeStack.size() + index;
        }
        index = Math.max(index, 0);
        index = Math.min(index, _modeStack.size());

        // if an active mode is removed, exit it first
        final AppMode mode = _modeStack.get(index);
        mode.within(new Runnable() {
            @Override public void run () {
                if (mode._active) {
                    mode.exitInternal();
                }
                mode.destroyInternal();
            }
        });

        _modeStack.remove(index);
    }

    protected void clearModeStackNow ()
    {
        _pendingModeTransitionQueue.clear();
        if (!_modeStack.isEmpty()) {
            popAllModes();
            handleModeTransitions();
        }
    }

    protected boolean isDestroyed ()
    {
        return _destroyed;
    }

    protected void shutdown ()
    {
        clearModeStackNow();
        _modeStack = null;
        _pendingModeTransitionQueue = null;
        _topLayer.destroy();
        _topLayer = null;
        this.destroyed.emit();
    }

    protected final FlashbangApp _app;
    protected final String _name;
    protected GroupLayer _topLayer = PlayN.graphics().createGroupLayer();
    protected List<AppMode> _modeStack = Lists.newArrayList();
    protected List<PendingTransition> _pendingModeTransitionQueue = Lists.newArrayList();
    protected boolean _destroyed;

    protected static class PendingTransition {
        public final ModeTransition type;
        public final AppMode mode;
        public final int index;

        public PendingTransition (ModeTransition type, AppMode mode, int index) {
            this.type = type;
            this.mode = mode;
            this.index = index;
        }
    }
}
