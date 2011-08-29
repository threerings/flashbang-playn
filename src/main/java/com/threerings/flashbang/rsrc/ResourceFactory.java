//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.Json;

public abstract class ResourceFactory
{
    public abstract Resource create (String name, Json.Object json);
}
