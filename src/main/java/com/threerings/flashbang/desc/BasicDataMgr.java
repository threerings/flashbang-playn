//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import java.util.Collection;
import java.util.Map;

public class BasicDataMgr<T extends NamedDataDesc> extends DataMgr<T>
{
    public BasicDataMgr (Class<T> klass)
    {
        super(klass);
    }

    public void resolveRefs (Map<Class<?>, DataMgr<?>> mgrs)
    {
        for (T desc : _data.values()) {
            desc.resolveRefs(mgrs);
        }
    }

    /**
     * Expose this method
     */
    @Override
    public void addData (T data)
    {
        super.addData(data);
    }

    public void addAll (Collection<? extends T> data)
    {
        for (T desc : data) {
            addData(desc);
        }
    }
}
