//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import java.util.Map;

import playn.core.Image;
import playn.core.Json;
import playn.core.Sound;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public class ResourceManager
{
    public Resource<?> getResource (String name)
    {
        return _resources.get(name);
    }

    public Sound getSound (String name)
    {
        Resource<?> rsrc = getResource(name);
        if (rsrc == null) {
            return null;
        }
        Preconditions.checkState(rsrc instanceof SoundResource);
        return ((SoundResource) rsrc).get();
    }

    public Image getImage (String name)
    {
        Resource<?> rsrc = getResource(name);
        if (rsrc == null) {
            return null;
        }
        Preconditions.checkState(rsrc instanceof ImageResource);
        return ((ImageResource) rsrc).get();
    }

    public Json.Object getJson (String name)
    {
        Resource<?> rsrc = getResource(name);
        if (rsrc == null) {
            return null;
        }
        Preconditions.checkState(rsrc instanceof JsonResource);
        return ((JsonResource) rsrc).get();
    }

    public String getText (String name)
    {
        Resource<?> rsrc = getResource(name);
        if (rsrc == null) {
            return null;
        }
        Preconditions.checkState(rsrc instanceof TextResource);
        return ((TextResource) rsrc).get();
    }

    public boolean isResourceLoaded (String name)
    {
        return (getResource(name) != null);
    }

    public void unloadAll ()
    {
        _resources.clear();
    }

    void addAll (Iterable<Resource<?>> resources)
    {
        for (Resource<?> rsrc : resources) {
            Resource<?> old = _resources.put(rsrc.path, rsrc);
            Preconditions.checkState(old == null,
                "A resource with that name already exists [name=%s]", rsrc.path);
        }
    }

    protected Map<String, Resource<?>> _resources = Maps.newHashMap();
}
