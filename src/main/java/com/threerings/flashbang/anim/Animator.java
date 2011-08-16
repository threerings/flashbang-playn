//
// Flashbang - a framework for creating PlayN games
// Copyright (C) 2011 Three Rings Design, Inc., All Rights Reserved
// http://github.com/threerings/flashbang-playn

package com.threerings.flashbang.anim;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;

import com.threerings.flashbang.GameObject;
import com.threerings.flashbang.anim.desc.AnimDesc;
import com.threerings.flashbang.anim.desc.KeyframeDesc;

public class Animator extends GameObject
{
    public Animator (AnimDesc desc)
    {
        _desc = desc;
    }

    public AnimDesc animDesc ()
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

    @Override
    protected void update (float dt)
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

    protected KeyframeDesc curKeyframe ()
    {
        if (_curKeyframe == null) {
            _curKeyframe = _desc.getKeyframe(_curFrame);
        }
        return _curKeyframe;
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

    protected final AnimDesc _desc;

    protected int _curFrame;
    protected boolean _stopped;
    protected float _elapsedTime;

    protected KeyframeDesc _curKeyframe;
}
