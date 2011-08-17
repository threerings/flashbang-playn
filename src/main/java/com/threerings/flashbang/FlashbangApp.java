//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang;

import static playn.core.PlayN.graphics;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.SurfaceLayer;
import pythagoras.i.Point;

public abstract class FlashbangApp
    implements Game
{
    /**
     * Returns the approximate frames-per-second that the application
     * is running at.
     */
    public float fps ()
    {
        return _fps;
    }

    /**
    * Creates and registers a new Viewport. (Flashbang automatically creates a Viewport on
    * initialization, so this is only necessary for creating additional ones.)
    *
    * Viewports must be uniquely named.
    */
   public Viewport createViewport (String name, GroupLayer parentLayer)
   {
       Viewport viewport = new Viewport(this, name, parentLayer);
       Object existing = _viewports.put(name, viewport);
       Preconditions.checkState(existing == null, "A Viewport named '%s' already exists", name);
       return viewport;
   }

   /**
    * Creates and registers a new Viewport attached to the rootLayer.
    */
   public Viewport createViewport (String name)
   {
       return createViewport(name, PlayN.graphics().rootLayer());
   }

   /**
    * Returns the Viewport with the given name, if it exists.
    */
   public Viewport getViewport (String name)
   {
       return _viewports.get(name);
   }

   /**
    * Returns the default Viewport that was created when Flashbang was initialized
    */
   public Viewport defaultViewport ()
   {
       return getViewport(Viewport.DEFAULT);
   }

   @Override
   public void init ()
   {
       Point screenSize = screenSize();
       graphics().setSize(screenSize.x, screenSize.y);

       // Create our background
       SurfaceLayer bg = graphics().createSurfaceLayer(screenSize.x, screenSize.y);
       bg.setDepth(-Float.MAX_VALUE);
       bg.surface().setFillColor(0xffffffff);
       bg.surface().fillRect(0, 0, screenSize.x, screenSize.y);
       graphics().rootLayer().add(bg);

       // Create our default viewport
       createViewport(Viewport.DEFAULT);
   }

   /**
    * @return the desired screen size
    */
   public abstract Point screenSize ();

   @Override
   public void update (float deltaMillis)
   {
       // We do our work in seconds
       float dt = deltaMillis * 0.001f;

       _fps = 1 / dt;

       // update our viewports
       for (Viewport viewport : Lists.newArrayList(_viewports.values())) {
           if (!viewport.isDestroyed()) {
               viewport.update(dt);
           }
           if (viewport.isDestroyed()) {
               _viewports.remove(viewport.name());
               viewport.shutdown();
           }
       }
   }

   @Override
   public void paint (float alpha)
   {
   }

   protected float _fps;

   protected Map<String, Viewport> _viewports = Maps.newHashMap();
}
