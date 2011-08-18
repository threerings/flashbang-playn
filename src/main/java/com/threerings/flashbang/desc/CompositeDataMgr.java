//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import react.Slot;

/**
 * A DataMgr that combines multiple DataMgrs that share a common type.
 */
public class CompositeDataMgr<T extends NamedDataDesc> extends DataMgr<T>
{
    public CompositeDataMgr (Class<T> klass)
    {
        super(klass);
    }

    public CompositeDataMgr (Class<T> klass, DataMgr<? extends T> mgr)
    {
        this(klass);
        addMgr(mgr);
    }

    public CompositeDataMgr (Class<T> klass, DataMgr<? extends T> mgr1, DataMgr<? extends T> mgr2)
    {
        this(klass, mgr1);
        addMgr(mgr2);
    }

    public CompositeDataMgr (Class<T> klass, DataMgr<? extends T> mgr1, DataMgr<? extends T> mgr2,
        DataMgr<? extends T> mgr3)
    {
        this(klass, mgr1, mgr2);
        addMgr(mgr3);
    }

    public CompositeDataMgr (Class<T> klass, DataMgr<? extends T> mgr1, DataMgr<? extends T> mgr2,
        DataMgr<? extends T> mgr3, DataMgr<? extends T> mgr4)
    {
        this(klass, mgr1, mgr2, mgr3);
        addMgr(mgr4);
    }

    public CompositeDataMgr (Class<T> klass, Iterable<DataMgr<? extends T>> mgrs)
    {
        this(klass);
        for (DataMgr<? extends T> mgr : mgrs) {
            addMgr(mgr);
        }
    }

    protected void addMgr (DataMgr<? extends T> mgr)
    {
        mgr.dataAdded.connect(new Slot<NamedDataDesc>() {
            @Override public void onEmit (NamedDataDesc data) {
                addData(_klass.cast(data));
            }
        });

        for (DataDesc data : mgr.getAllData()) {
            addData(_klass.cast(data));
        }
    }
}
