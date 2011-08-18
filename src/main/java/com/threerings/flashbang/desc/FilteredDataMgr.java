//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import react.Slot;

/**
 * A DataMgr that filters another DataMgr, based on type.
 */
public class FilteredDataMgr<T extends NamedDataDesc> extends DataMgr<T>
{
    public FilteredDataMgr (Class<T> klass, DataMgr<? super T> mgr)
    {
        super(klass);
        mgr.dataAdded.connect(new Slot<NamedDataDesc>() {
            @Override public void onEmit (NamedDataDesc data) {
                if (_klass.isInstance(data)) {
                    addData(_klass.cast(data));
                }
            }
        });

        for (DataDesc data : mgr.getAllData()) {
            if (_klass.isInstance(data)) {
                addData(_klass.cast(data));
            }
        }
    }

    protected DataMgr<? super T> _filteredMgr;
}
