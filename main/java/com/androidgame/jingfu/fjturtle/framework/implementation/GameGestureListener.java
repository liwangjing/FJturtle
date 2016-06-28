package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by jing on 2016/6/28.
 */
public class GameGestureListener extends GestureDetector.SimpleOnGestureListener {

    private int width;

    public GameGestureListener(int width) {
        this.width = width;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > 0) {
            leftSwipe();
        } else if (e2.getX() - e1.getX() > 0) {
            rightSwipe();
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (e.getX() < width/2 )
        {
            leftDoubleTap();
        } else {
            rightDoubleTap();
        }
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (e.getX() < width/2 )
        {
            leftSingleTap();
        } else {
            rightSingleTap();
        }
        return super.onSingleTapConfirmed(e);
    }

    public void leftSwipe() {

    }

    public void rightSwipe() {

    }

    public void leftDoubleTap() {

    }
    public void rightDoubleTap() {

    }

    public void leftSingleTap() {

    }

    public void rightSingleTap() {

    }
}
