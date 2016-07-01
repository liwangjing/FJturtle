package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.v4.view.MotionEventCompat;

import com.androidgame.jingfu.fjturtle.framework.Input.TouchEvent;
import com.androidgame.jingfu.fjturtle.framework.Pool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsomemark on 6/15/16.
 */
public class MultiTouchHandler implements TouchHandler {

    private static final int MAX_TOUCHPOINTS = 10;
//    private static int l = 0;
    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
    int[] touchX = new int[MAX_TOUCHPOINTS];
    int[] touchY = new int[MAX_TOUCHPOINTS];
    int[] id = new int[MAX_TOUCHPOINTS];
    Pool<TouchEvent> touchEventPool;
    List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;
    int pointerCounter = -1;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return isTouched[index];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    @Override
    public int getPointerCounter() {
        synchronized (this) {
            return pointerCounter;
        }
    }

    private int getIndex(int pointerId) {
        synchronized (this) {
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                if (id[i] == pointerId)
                    return i;
            }
            return -1;
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
    public boolean onTouch(View v, MotionEvent event) { // motion event contains the information of the event, number of pointers and motion events of other pointers.
        synchronized (this) {
            int action = MotionEventCompat.getActionMasked(event); //return the event triggered this method.
            int pointerIndex = MotionEventCompat.getActionIndex(event); // return the index of the pointer that triggered this event.
            int pointerCount = pointerCounter = event.getPointerCount(); // return the number of pointers on the screen.
            TouchEvent touchEvent;
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                if (i >= pointerCount) {
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                } //保留 pointerCount数量的数组空间，其他数字位置都清空，XY坐标可以忽略。
                //在pointerCount范围内的，进入一下代码。
                int pointerId = event.getPointerId(i);// 传入的的是pointer的index，既是按活动顺序编号的index，返回那个pointer在系统中的id
                // Return the pointer identifier associated with a particular pointer data index in this event.
                // The identifier tells you the actual pointer number associated with the data

                /* 这个判断条件有问题 */
                if (action == MotionEvent.ACTION_MOVE || i == pointerIndex) { //若动作是MOVE 或者 此时处理的正是触发这个onTouch方法的pointer。
                    switch (action) {
                        // pointer touches the screen
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_POINTER_DOWN:
                            touchEvent = touchEventPool.newObject();
                            touchEvent.type = TouchEvent.TOUCH_DOWN;
                            touchEvent.pointer = pointerId;
                            touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                            touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                            isTouched[i] = true;
                            id[i] = pointerId;  // 在id[]的第i个位置，存放pointer的id.id与index不同！！
                            touchEventsBuffer.add(touchEvent);
                            break;
                        // pointer lifts up
                        case MotionEvent.ACTION_UP: //Constant Value: 1
                        case MotionEvent.ACTION_POINTER_UP: //Constant Value: 6
                        case MotionEvent.ACTION_CANCEL:
                            touchEvent = touchEventPool.newObject();
                            touchEvent.type = TouchEvent.TOUCH_UP;
                            touchEvent.pointer = pointerId;
                            touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                            touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                            isTouched[i] = false;
                            id[i] = -1;
                            touchEventsBuffer.add(touchEvent);
                            if (pointerCounter == 1)
                                pointerCounter = 0;
                            break;
                        // pointer moves
                        case MotionEvent.ACTION_MOVE:
                            touchEvent = touchEventPool.newObject();
                            touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                            touchEvent.pointer = pointerId;
                            touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                            touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                            isTouched[i] = true;
                            id[i] = pointerId;
                            touchEventsBuffer.add(touchEvent);
                            break;
                    }
                }
            }
            return true;
        }
    }
}
