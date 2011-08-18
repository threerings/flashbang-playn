//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.desc;

import com.google.common.base.Function;

public interface NamedDataDesc extends DataDesc
{
    public static final int NULL_ID = 0;

    public int getId ();

    public String getName ();

    /** A function that converts a DataDesc to its Integer ID */
    public static final Function<NamedDataDesc, Integer> TO_ID =
        new Function<NamedDataDesc, Integer>() {
            @Override
            public Integer apply (NamedDataDesc desc)
            {
                return desc.getId();
            }
        };

}
