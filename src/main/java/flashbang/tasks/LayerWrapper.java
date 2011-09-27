//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.tasks;

import playn.core.Layer;
import flashbang.components.AlphaComponent;
import flashbang.components.DepthComponent;
import flashbang.components.LocationComponent;
import flashbang.components.RotationComponent;
import flashbang.components.ScaleComponent;
import flashbang.components.VisibleComponent;

public class LayerWrapper
    implements AlphaComponent, DepthComponent, LocationComponent, RotationComponent, ScaleComponent,
        VisibleComponent
{
    public LayerWrapper (Layer layer)
    {
        _layer = layer;
    }

    // AlphaComponent
    public float alpha () { return _layer.alpha(); }
    public void setAlpha (float alpha) { _layer.setAlpha(alpha); }

    // DepthComponent
    public float depth () { return _layer.depth(); }
    public void setDepth (float depth) { _layer.setDepth(depth); }

    // Location Component
    public float x () { return _layer.transform().tx(); }
    public float y () { return _layer.transform().ty(); }
    public void setX (float x) { _layer.transform().setTx(x); }
    public void setY (float y) { _layer.transform().setTy(y); }

    // Rotation Component
    public float rotation () { return _layer.transform().rotation(); }
    public void setRotation (float angle) { _layer.transform().setRotation(angle); }

    // ScaleComponent
    public float scaleX () { return _layer.transform().scaleX(); }
    public float scaleY () { return _layer.transform().scaleY(); }
    public void setScaleX (float scaleX) { _layer.transform().setScaleX(scaleX); }
    public void setScaleY (float scaleY) { _layer.transform().setScaleY(scaleY); }

    // VisibleComponent
    public boolean visible () { return _layer.visible(); }
    public void setVisible (boolean visible) { _layer.setVisible(visible); }

    protected final Layer _layer;
}
