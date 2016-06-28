package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.view.View;


import com.androidgame.jingfu.fjturtle.framework.Input.TouchEvent;

import java.util.List;

/**
 * Created by handsomemark on 6/15/16.
 */
public interface TouchHandler extends View.OnTouchListener{
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}
