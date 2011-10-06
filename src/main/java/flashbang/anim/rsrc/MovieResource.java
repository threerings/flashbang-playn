//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import com.google.common.base.Preconditions;

import playn.core.Json;

import flashbang.Flashbang;
import flashbang.anim.Movie;
import flashbang.anim.rsrc.EditableMovieConf;
import flashbang.rsrc.Resource;
import flashbang.rsrc.ResourceFactory;

public class MovieResource extends Resource
{
    public static final String TYPE = "model";

    public static final ResourceFactory FACTORY = new ResourceFactory() {
        @Override public Resource create (String name, Json.Object json) {
            Json.TypedArray<Json.Object> movie = json.getArray("children", Json.Object.class);
            return new MovieResource(name, new EditableMovieConf(movie));
        }
    };

    public static Movie require (String name) {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkNotNull(rsrc, "No such resource '%s'", name);
        Preconditions.checkState(rsrc instanceof MovieResource,
            "'%s' is a '%s', not a MovieResource", name, rsrc.getClass().getName());
        return ((MovieResource)rsrc)._conf.build();
    }

    public MovieResource (String name, MovieConf conf) {
        super(name);
        _conf = conf;
    }

    @Override protected void doLoad () {
        loadComplete(null);
    }

    protected final MovieConf _conf;
}
