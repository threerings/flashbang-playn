//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.List;

import com.google.common.base.Preconditions;

public class AnimDesc
{
    public enum EndBehavior {
        STOP,
        LOOP;
    }

    public List<KeyframeDesc> keyframes;
    public EndBehavior endBehavior;
    public float framerate;

    /**
     * @return The total number of frames in the animation
     */
    public int totalFrames ()
    {
        return _totalFrames;
    }

    /**
     * @return the KeyframeDesc for the given frameIdx
     */
    public KeyframeDesc getKeyframe (int frameIdx)
    {
        Preconditions.checkArgument(frameIdx >= 0 && frameIdx < _totalFrames,
            "frameIdx out bounds [frameIdx=%s, totalFrames=%s]", frameIdx, _totalFrames);

        // binary-search
        int loIdx = 0;
        int hiIdx = keyframes.size() - 1;
        for (;;) {
            int idx = loIdx + ((hiIdx - loIdx) >>> 1);
            KeyframeDesc kf = keyframes.get(idx);
            if (frameIdx < kf.frameIdx) {
                // too high
                hiIdx = idx - 1;
            } else if (frameIdx > kf.endFrameIdx()) {
                // too low
                loIdx = idx + 1;
            } else {
                // hit!
                return kf;
            }
        }
    }

    public void init ()
    {
        Preconditions.checkState(!keyframes.isEmpty(),
            "An animation must consist of at least one keyframe");

        _totalFrames = keyframes.get(keyframes.size() - 1).frameIdx + 1;

        // Give each keyframe a pointer to its next keyframe, for faster interpolation
        int lastKeyframeIdx = -1;
        for (int ii = 0; ii < keyframes.size(); ++ii) {
            KeyframeDesc kf = keyframes.get(ii);
            Preconditions.checkState(kf.frameIdx > lastKeyframeIdx,
                "keyframe %s has an invalid frameIdx (<= previous)", ii);

            if (ii < keyframes.size() - 1) {
                kf.next = keyframes.get(ii + 1);
            }
        }
    }

    protected int _totalFrames;
}
