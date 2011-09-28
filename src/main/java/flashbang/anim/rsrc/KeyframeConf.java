//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package flashbang.anim.rsrc;

import tripleplay.util.Interpolator;

public interface KeyframeConf
{
    /**
     * Returns the start frame for this keyframe. The keyframe is active until the start frame of
     * the keyframe returned by next if it isn't null. If it is null, the keyframe is active until
     * the end of the animation.
     */
    int frame();

    /** Returns the value at the start frame for this keyframe. */
    float value();

    Interpolator interpolator();

    /** Returns the keyframe after this one, or null if this is the last keyframe.  */
    KeyframeConf next();

    /**
     * Finds the appropriate frame to interpolate the given frame if it's at or after this frame.
     * If the frame is before this one, the behavior is undefined.
     */
    KeyframeConf find (int frame);

    /**
     * Interpolates the value for this keyframe for the given frame. If the given frame isn't
     * covered by this keyframe, the behavior is undefined.
     */
    float interp (int frame);
}
