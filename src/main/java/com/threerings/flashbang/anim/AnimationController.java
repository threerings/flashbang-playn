//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import com.threerings.flashbang.anim.desc.ModelAnimDesc;
import com.threerings.flashbang.anim.desc.KeyframeDesc;

public class AnimationController
{
    public AnimationController (ModelAnimDesc anim)
    {
        _desc = anim;
    }

    public ModelAnimDesc animDesc ()
    {
        return _desc;
    }

    public int curFrame ()
    {
        return _curFrame;
    }

    public void setCurFrame (int curFrame)
    {
        setCurFrameInternal(curFrame);
        _elapsedTime = (_curFrame / _desc.framerate);
    }

    public boolean stopped ()
    {
        return _stopped;
    }

    public void setStopped (boolean stopped)
    {
        _stopped = stopped;
    }

    public void update (Model model, float dt)
    {
        if (_stopped) {
            return;
        }

        // Calculate our current frame
        _elapsedTime += dt;
        int newFrame = (int) (_desc.framerate * _elapsedTime);

        // Apply end behavior
        if (newFrame >= _desc.totalFrames()) {
            switch (_desc.endBehavior) {
            case STOP:
                newFrame = _desc.totalFrames() - 1;
                break;

            case LOOP:
                newFrame %= _desc.totalFrames();
                break;
            }
        }

        if (newFrame != _curFrame) {
            setCurFrameInternal(newFrame);
            // TODO: apply the animation!
        }
    }

    protected void setCurFrameInternal (int frame)
    {
        if (_curFrame == frame) {
            return;
        }

        Preconditions.checkArgument(frame >= 0 && frame < _desc.totalFrames(),
            "Frame out of bounds [frame=%s, totalFrames=%s]", frame, _desc.totalFrames());

        _curFrame = frame;

        // Invalidate our current keyframe?
        if (_curKeyframe != null && !_curKeyframe.validForFrame(_curFrame)) {
            // Can we save ourselves from looking up our keyframe again?
            if (_curKeyframe.next() != null && _curKeyframe.next().validForFrame(_curFrame)) {
                _curKeyframe = _curKeyframe.next();
            } else {
                _curKeyframe = null;
            }
        }
    }

    protected final ModelAnimDesc _desc;

    protected int _curFrame;
    protected boolean _stopped;
    protected float _elapsedTime;

    protected KeyframeDesc _curKeyframe;
}
