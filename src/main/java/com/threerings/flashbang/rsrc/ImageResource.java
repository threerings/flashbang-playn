//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.ResourceCallback;

public class ImageResource extends Resource<Image>
{
    public ImageResource (String path)
    {
        super(path);
    }

    @Override
    protected void doLoad ()
    {
        PlayN.assetManager().getImage(path).addCallback(new ResourceCallback<Image>() {
            @Override public void done (Image rsrc) {
                _image = rsrc;
                loadComplete(null);
            }
            @Override public void error (Throwable err) {
                loadComplete(err);
            }
        });
    }

    @Override
    public Image get ()
    {
        return _image;
    }

    protected Image _image;
}
