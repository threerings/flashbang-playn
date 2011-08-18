//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import java.util.Map;

import playn.core.Json;

public interface DataDesc
{
    void resolveRefs (DataMgr<?>... mgrs);
    void resolveRefs (Map<Class<?>, DataMgr<?>> mgrs);
    void fromJson (Json.Object json);
}
