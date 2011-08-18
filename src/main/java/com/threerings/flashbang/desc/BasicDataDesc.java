//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

public abstract class BasicDataDesc implements DataDesc
{
    /* (non-Javadoc)
     * @see com.threerings.who.datadesc.IDataDesc#resolveRefs(com.threerings.who.datadesc.DataMgr)
     */
    public void resolveRefs (DataMgr<?>...mgrs)
    {
        resolveRefs(DataMgr.buildDataMgrMap(mgrs));
    }
}
