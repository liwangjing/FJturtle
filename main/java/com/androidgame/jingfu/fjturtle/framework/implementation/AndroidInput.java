package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.androidgame.jingfu.fjturtle.framework.Input;

import java.util.List;


/**
 * Created by handsomemark on 6/14/16.
 */
public class AndroidInput implements Input {

    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        if (Build.VERSION.SDK_INT < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    } // template pattern. same function name is for easy recognization.

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
