//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import react.Signal;

public abstract class DataMgr<T extends NamedDataDesc>
{
    public final Signal<T> dataAdded = new Signal<T>();

    /**
     * Returns a mapping of DataDesc types to DataMgrs (used in DescRef resolution)
     */
    public static Map<Class<?>, DataMgr<?>> buildDataMgrMap (DataMgr<?>... dataMgrs)
    {
        Map<Class<?>, DataMgr<?>> mgrMap = Maps.newHashMap();
        for (DataMgr<?> mgr : dataMgrs) {
            DataMgr<?> cur = mgrMap.put(mgr.getDescClass(), mgr);
            Preconditions.checkState(cur == null,
                "Multiple DataMgrs with the same DescType [type=%s, mgr1=%s, mgr2=%s]",
                mgr.getDescClass(), mgr, cur);
        }
        return mgrMap;
    }

    public DataMgr (Class<T> klass)
    {
        _klass = klass;
    }

    public Class<T> getDescClass ()
    {
        return _klass;
    }

    public int size ()
    {
        return _data.size();
    }

    public T requireData (int id)
    {
        T data = getData(id);
        Preconditions.checkNotNull(data, "No such %s [id=%s]", _klass.getSimpleName(), id);
        return data;
    }

    public T requireData (String name)
    {
        T data = getData(name);
        Preconditions.checkState(data != null, "No such %s [name=%s]", _klass.getSimpleName(),
            name);
        return data;
    }

    @SuppressWarnings("unchecked")
    public <X extends T> X requireData (int id, Class<X> clazz)
    {
        T data = requireData(id);
        Preconditions.checkState(clazz.isInstance(data), "Can't convert to %s [id=%s]",
            clazz.getSimpleName(), id);
        return (X) data;
    }

    public T getData (int id)
    {
        return _data.get(id);
    }

    @SuppressWarnings("unchecked")
    public <X extends T> X getData (int id, Class<X> clazz)
    {
        T data = getData(id);
        return (data != null && clazz.isInstance(data) ? (X) data : null);
    }

    public T getData (String name)
    {
        return getData(DataDescUtil.nameToId(name));
    }

    public <X extends T> X getData (String name, Class<X> clazz)
    {
        return getData(DataDescUtil.nameToId(name), clazz);
    }

    public Collection<T> getAllData ()
    {
        return _data.values();
    }

    protected void addData (T data)
    {
        Preconditions.checkArgument(data.getName().length() > 0, "data name is null [data=%s]",
            data);

        int id = data.getId();
        Preconditions.checkState(id != 0, "bad id (cannot be 0) [data=%s]", data);

        T old = _data.put(id, data);
        Preconditions.checkState(old == null, "Duplicate DataDesc ids [first=%s, second=%s]",
            old, data);

        dataAdded.emit(data);
    }

    protected Map<Integer, T> _data = Maps.newHashMap();

    protected final Class<T> _klass;
}
