//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim.desc;

import java.util.List;

import com.google.common.base.Preconditions;

/**
 * LayerAnimDesc describes how to animate a single layer in a Model.
 */
public class LayerAnimDesc
{
    public String layerSelector;
    public List<KeyframeDesc> keyframes;

    /**
     * @return The total number of frames in the animation
     */
    public int numFrames ()
    {
        return _numFrames;
    }

    /**
     * @return the KeyframeDesc for the given frameIdx. If frameIdx >= _totalFrames,
     * returns the last KeyFrameDesc in the list.
     */
    public KeyframeDesc getKeyframe (int frameIdx)
    {
        Preconditions.checkArgument(frameIdx >= 0);

        if (frameIdx >= _numFrames) {
            return keyframes.get(keyframes.size() - 1);
        }

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

        _numFrames = keyframes.get(keyframes.size() - 1).frameIdx + 1;

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

    protected int _numFrames;
}
