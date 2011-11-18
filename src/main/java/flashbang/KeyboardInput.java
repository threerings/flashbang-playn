//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang;

import playn.core.Keyboard;
import react.Signal;

public class KeyboardInput
{
    public final Signal<Keyboard.Event> keyDown = Signal.create();
    public final Signal<Keyboard.Event> keyUp = Signal.create();
    public final Signal<Keyboard.TypedEvent> keyTyped = Signal.create();
}
