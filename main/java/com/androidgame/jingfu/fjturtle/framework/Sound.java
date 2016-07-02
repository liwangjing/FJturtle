package com.androidgame.jingfu.fjturtle.framework;

/**
 * Created by handsomemark on 6/14/16.
 */
public interface Sound {

    public void play(float volume);  //表示最大音量的百分比。

    public void play(float volume, float playRate);  //表示最大音量的百分比。

    public void dispose();
}
