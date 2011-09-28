//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.rsrc;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import playn.core.Image;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.ResourceCallback;

import pythagoras.f.Rectangle;

import tripleplay.util.JsonUtil;

import flashbang.Flashbang;

public class ImageResource extends Resource
{
    public static final String TYPE = "image";

    public static ResourceFactory FACTORY = new ResourceFactory() {
        @Override public Resource create (String name, Json.Object json) {
            List<Rectangle> frameRects = null;
            if (json.getObject("frameRects") != null) {
                frameRects = Lists.newArrayList();
                for (Json.Object jsonRect : json.getArray("frameRects", Json.Object.class)) {
                    Rectangle r = new Rectangle();
                    r.x = JsonUtil.requireFloat(jsonRect, "x");
                    r.y = JsonUtil.requireFloat(jsonRect, "y");
                    r.width = JsonUtil.requireFloat(jsonRect, "width");
                    r.height = JsonUtil.requireFloat(jsonRect, "height");
                }
            }

            return new ImageResource(name, frameRects);
        }
    };

    public static ImageResource require (String name)
    {
        Resource rsrc = Flashbang.rsrcs().requireResource(name);
        Preconditions.checkState(rsrc instanceof ImageResource,
            "Not an ImageResource [name=%s]", name);
        return (ImageResource) rsrc;
    }

    public final List<Rectangle> frameRects;

    public ImageResource (String path, List<Rectangle> frameRects)
    {
        super(path);
        this.frameRects = frameRects;
    }

    public ImageResource (String path)
    {
        this(path, null);
    }

    @Override
    protected void doLoad ()
    {
        PlayN.assetManager().getImage(name).addCallback(new ResourceCallback<Image>() {
            @Override public void done (Image rsrc) {
                _image = rsrc;
                loadComplete(null);
            }
            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    public Image image ()
    {
        return _image;
    }

    protected Image _image;
}
