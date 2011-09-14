//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import playn.core.Layer;

import pythagoras.f.IPoint;
import pythagoras.f.IVector;
import pythagoras.f.Point;
import pythagoras.f.Vector;

import com.threerings.flashbang.components.AlphaComponent;
import com.threerings.flashbang.components.DepthComponent;
import com.threerings.flashbang.components.LayerComponent;
import com.threerings.flashbang.components.LocationComponent;
import com.threerings.flashbang.components.RotationComponent;
import com.threerings.flashbang.components.ScaleComponent;
import com.threerings.flashbang.components.VisibleComponent;

public abstract class SceneObject extends GameObject
    implements AlphaComponent, DepthComponent, LayerComponent, LocationComponent, RotationComponent,
        ScaleComponent, VisibleComponent
{
    // AlphaComponent
    public float alpha () { return layer().alpha(); }
    public void setAlpha (float alpha) { layer().setAlpha(alpha); }

    // DepthComponent
    public float depth () { return layer().depth(); }
    public void setDepth (float depth) { layer().setDepth(depth); }

    // Location Component
    public float x () { return layer().transform().tx(); }
    public float y () { return layer().transform().ty(); }
    public void setX (float x) { layer().transform().setTx(x); }
    public void setY (float y) { layer().transform().setTy(y); }

    public Point loc () { return new Point(x(), y()); }
    public void setLoc (IPoint loc) { setX(loc.x()); setY(loc.y()); }
    public void setLoc (float x, float y) { setX(x); setY(y); }

    // Rotation Component
    public float rotation () { return layer().transform().rotation(); }
    public void setRotation (float angle) { layer().transform().setRotation(angle); }

    // ScaleComponent
    public float scaleX () { return layer().transform().scaleX(); }
    public float scaleY () { return layer().transform().scaleY(); }
    public void setScaleX (float scaleX) { layer().transform().setScaleX(scaleX); }
    public void setScaleY (float scaleY) { layer().transform().setScaleY(scaleY); }

    public Vector scale () { return layer().transform().scale(); }
    public void setScale (IVector scale) { setScaleX(scale.x()); setScaleY(scale.y()); }
    public void setScale (float scaleX, float scaleY) { setScaleX(scaleX); setScaleY(scaleY); }

    // VisibleComponent
    public boolean visible () { return layer().visible(); }
    public void setVisible (boolean visible) { layer().setVisible(visible); }

    // Utility functions
    public Point screenToLocal (IPoint screen)
    {
        return Layer.Util.screenToLayer(layer(), screen.x(), screen.y());
    }

    public Point localToScreen (IPoint local)
    {
        return Layer.Util.layerToScreen(layer(), local.x(), local.y());
    }

    @Override
    protected void cleanup ()
    {
        super.cleanup();

        // Destroy our layer
        Layer layer = layer();
        if (layer != null && !layer.destroyed()) {
            layer.destroy();
        }
    }
}
