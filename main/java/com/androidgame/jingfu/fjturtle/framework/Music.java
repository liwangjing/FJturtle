package com.androidgame.jingfu.fjturtle.framework;

/**
 * Created by handsomemark on 6/14/16.
 */
public interface Music {

    public void play();

    public void stop();

    public void pause();

    public void setLooping(boolean looping);

    public void setVolume(float volume);

    public boolean isPlaying();

    public boolean isStopped();

    public boolean isLooping();

    public void dispose();//dispose = 部署

    void seekBegin();
}
