package com.androidgame.jingfu.fjturtle.framework;

import java.util.List;

/**
 * Created by  on 6/14/16.
 */
public interface Input { //inout的内容会有很多，不仅限于touch，从发展的眼光来看，为了之后添加新的input，所以要有这个class。

    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;
    }

    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}
