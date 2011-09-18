//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.desc;

import java.util.Collection;

public class BasicDataMgr<T extends NamedDataDesc> extends DataMgr<T>
{
    public BasicDataMgr (Class<T> klass)
    {
        super(klass);
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
