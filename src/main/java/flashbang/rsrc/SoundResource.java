//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.rsrc;

import com.google.common.base.Preconditions;

import playn.core.Json;
import playn.core.PlayN;
import playn.core.Sound;

import flashbang.Flashbang;

public class SoundResource extends Resource
{
    public static final String TYPE = "sound";
    public static final ResourceFactory FACTORY = new ResourceFactory() {
        @Override public Resource create (String name, Json.Object json) {
            return new SoundResource(name);
        }
    };

    public static SoundResource require (String name) {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkState(rsrc instanceof SoundResource,
            "Not a SoundResource [name=%s]", name);
        return (SoundResource) rsrc;
    }

    public SoundResource (String path) {
        super(path);
    }

    @Override protected void doLoad () {
        _sound = PlayN.assets().getSound(name);
        // sounds currently load immediately
        loadComplete(null);
    }

    public Sound sound () { return _sound; }

    protected Sound _sound;
}
