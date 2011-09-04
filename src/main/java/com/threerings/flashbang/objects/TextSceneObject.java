//
// Nod - Copyright 2011 Three Rings Design, Inc.

package com.threerings.flashbang.objects;

import playn.core.CanvasLayer;
import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.TextFormat;
import playn.core.TextLayout;

import com.threerings.flashbang.SceneObject;
import com.threerings.flashbang.components.TextComponent;

public class TextSceneObject extends SceneObject
    implements TextComponent
{
    public TextSceneObject (String text, TextFormat format)
    {
        _groupLayer = PlayN.graphics().createGroupLayer();
        _text = text;
        _format = format;
        redraw();
    }

    public TextSceneObject (String text)
    {
        this(text, DEFAULT_FORMAT);
    }

    @Override
    public Layer layer ()
    {
        return _groupLayer;
    }

    public String text ()
    {
        return _text;
    }

    public void setText (String text)
    {
        if (!_text.equals(text)) {
            _text = text;
            redraw();
        }
    }

    public TextFormat textFormat ()
    {
        return _format;
    }

    public void setTextFormat (TextFormat format)
    {
        _format = format;
        redraw();
    }

    public int width ()
    {
        return _canvasLayer.canvas().width();
    }

    public int height ()
    {
        return _canvasLayer.canvas().height();
    }

    protected void redraw ()
    {
        TextLayout layout = PlayN.graphics().layoutText(_text, _format);

        int width = (int) (layout.width() + 0.5f);
        int height = (int) (layout.height() + 0.5f);

        // If our dimensions haven't changed, reuse our canvas layer
        if (_canvasLayer != null) {
            if (_canvasLayer.canvas().width() != width ||
                _canvasLayer.canvas().height() != height) {
                _canvasLayer.destroy();
                _canvasLayer = null;
            } else {
                _canvasLayer.canvas().clear();
            }
        }

        if (_canvasLayer == null) {
            _canvasLayer = PlayN.graphics().createCanvasLayer(width, height);
            _groupLayer.add(_canvasLayer);
        }

        _canvasLayer.canvas().drawText(layout, 0, 0);
    }

    protected final GroupLayer _groupLayer;
    protected CanvasLayer _canvasLayer;
    protected String _text;
    protected TextFormat _format;

    protected static final TextFormat DEFAULT_FORMAT = new TextFormat();
}
