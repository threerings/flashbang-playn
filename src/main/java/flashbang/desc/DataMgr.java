//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.desc;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import react.Signal;

public abstract class DataMgr<T extends NamedDataDesc>
{
    public final Signal<T> dataAdded = new Signal<T>();

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
        Preconditions.checkNotNull(data, "No such %s [id=%s]", _klass.getName(), id);
        return data;
    }

    public T requireData (String name)
    {
        T data = getData(name);
        Preconditions.checkState(data != null, "No such %s [name=%s]", _klass.getName(), name);
        return data;
    }

    public T getData (int id)
    {
        return _data.get(id);
    }

    public T getData (String name)
    {
        return getData(DataDescUtil.nameToId(name));
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
