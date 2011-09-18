//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.desc;

public class DataDescUtil
{
    public static int nameToId (String name)
    {
        return name == null ? NamedDataDesc.NULL_ID : name.hashCode();
    }
}
