//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.objects;

import playn.core.CanvasLayer;
import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.TextFormat;
import playn.core.TextLayout;

import flashbang.SceneObject;
import flashbang.components.TextComponent;

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

    @Override public Layer layer ()
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
        return _layoutWidth;
    }

    public int height ()
    {
        return _layoutHeight;
    }

    protected void redraw ()
    {
        TextLayout layout = PlayN.graphics().layoutText(_text, _format);

        _layoutWidth = (int) (layout.width() + 0.5f);
        _layoutHeight = (int) (layout.height() + 0.5f);

        // If our dimensions haven't gotten bigger, we can reuse our canvas
        if (_canvasLayer != null) {
            if (_canvasLayer.canvas().width() < _layoutWidth ||
                _canvasLayer.canvas().height() < _layoutHeight) {
                _canvasLayer.destroy();
                _canvasLayer = null;
            } else {
                _canvasLayer.canvas().clear();
            }
        }

        if (_canvasLayer == null) {
            _canvasLayer = PlayN.graphics().createCanvasLayer(_layoutWidth, _layoutHeight);
            _groupLayer.add(_canvasLayer);
        }

        _canvasLayer.canvas().drawText(layout, 0, 0);
    }

    protected final GroupLayer _groupLayer;
    protected CanvasLayer _canvasLayer;
    protected String _text;
    protected TextFormat _format;
    protected int _layoutWidth;
    protected int _layoutHeight;

    protected static final TextFormat DEFAULT_FORMAT = new TextFormat();
}
