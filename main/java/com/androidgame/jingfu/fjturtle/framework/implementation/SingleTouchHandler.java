package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.androidgame.jingfu.fjturtle.framework.Input.TouchEvent;
import com.androidgame.jingfu.fjturtle.framework.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsomemark on 6/15/16.
 */
public class SingleTouchHandler implements TouchHandler {

    boolean isTouched;
    int touchX;
    int touchY;
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;

    public SingleTouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        }; //通过匿名类来new 一个interface，需要override它的方法。因为这个interface是一个class的variable，相当于new了这个class，并继承这个interface
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            if (pointer == 0) //pointer 对应的是触碰手指在堆栈中的index，永远认为最近一个发生动作的手指是有效手指，即对应index为0.
                return isTouched;
            else
                return false;
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            return touchX;
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            return touchY;
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {  //synchronize statement。当这个方法被调用，方法对应的object的这个以及其他所有synchronized方法，都不能被其它线程访问。
            TouchEvent touchEvent = touchEventPool.newObject();
            switch (MotionEventCompat.getActionMasked(event)) {
                case MotionEvent.ACTION_DOWN:
                    touchEvent.type = TouchEvent.TOUCH_DOWN;
                    Log.d("Gesture:", "Down");
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    isTouched = true;
                    Log.d("Gesture:", "Move");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.d("Gesture:", "Cancel");
                case MotionEvent.ACTION_UP:
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    Log.d("Gesture:", "Up");
                    isTouched = false;
                    break;
            }
            touchEvent.x = touchX = (int) (event.getX() * scaleX);// unit is pixel
            touchEvent.y = touchY = (int) (event.getY() * scaleY);
            touchEventsBuffer.add(touchEvent);
            return true;
        }
    }
}
