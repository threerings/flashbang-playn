//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.rsrc;

import com.google.common.base.Preconditions;

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
    public void load (final ResourceCallback<? super Image> callback)
    {
        Preconditions.checkState(_image == null, "Already loaded");
        _image = PlayN.assetManager().getImage(path);
        _image.addCallback(new ResourceCallback<Image>() {
            @Override public void done (Image rsrc) {
                callback.done(rsrc);
            }
            @Override public void error (Throwable err) {
                callback.error(err);
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
