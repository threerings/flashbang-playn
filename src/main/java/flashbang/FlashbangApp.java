//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.PlayN;
import playn.core.Pointer;
import playn.core.SurfaceLayer;

import pythagoras.i.Point;

import flashbang.anim.rsrc.ModelResource;
import flashbang.rsrc.ImageResource;
import flashbang.rsrc.SoundResource;

import static playn.core.PlayN.graphics;

public abstract class FlashbangApp
    implements Game, Pointer.Listener
{
    public FlashbangApp ()
    {
        Flashbang.registerApp(this);
        Flashbang.rsrcs().registerFactory(ImageResource.TYPE, ImageResource.FACTORY);
        Flashbang.rsrcs().registerFactory(SoundResource.TYPE, SoundResource.FACTORY);
        Flashbang.rsrcs().registerFactory(ModelResource.TYPE, ModelResource.FACTORY);
    }

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

       // We'll handle all PlayN input events
       PlayN.pointer().setListener(this);
   }

   /**
    * @return the desired screen size
    */
   public abstract Point screenSize ();

   /**
    * @return the desired framerate (or 0 to run as fast as possible)
    */
   public int desiredFramerate ()
   {
       return 0;
   }

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

   @Override
   public void onPointerStart (final Pointer.Event event)
   {
       dispatchToTopAppModes(new AppModeOp() {
           @Override public void apply (AppMode mode) {
               mode.pointerListener().onPointerStart(event);
           }
       });
   }

   @Override
   public void onPointerEnd (final Pointer.Event event)
   {
       dispatchToTopAppModes(new AppModeOp() {
           @Override public void apply (AppMode mode) {
               mode.pointerListener().onPointerEnd(event);
           }
       });
   }

   @Override
   public void onPointerDrag (final Pointer.Event event)
   {
       dispatchToTopAppModes(new AppModeOp() {
           @Override public void apply (AppMode mode) {
               mode.pointerListener().onPointerDrag(event);
           }
       });
   }

   @Override
   public final int updateRate ()
   {
       return (desiredFramerate() > 0 ? 1000 / desiredFramerate() : 0);
   }

   protected void dispatchToTopAppModes (final AppModeOp op)
   {
       for (Viewport viewport : _viewports.values()) {
           if (!viewport.isDestroyed()) {
               final AppMode topMode = viewport.topMode();
               if (topMode != null) {
                   Flashbang.withinObjectDatabase(topMode, new Runnable() {
                       @Override public void run () {
                           op.apply(topMode);
                       }
                   });
               }
           }
       }
   }

   protected interface AppModeOp {
       void apply (AppMode mode);
   }

   protected float _fps;
   protected Map<String, Viewport> _viewports = Maps.newHashMap();
}
