package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.media.SoundPool;

import com.androidgame.jingfu.fjturtle.framework.Sound;


/**
 * Created by handsomemark on 6/15/16.
 */
public class AndroidSound implements Sound {

    int soundId;
    SoundPool soundPool; // 系统类型。SoundPool class manages and plays audio resources for applications.

    public AndroidSound(SoundPool soundPool, int soundId) { //播放声音范围与具体声音id已给定。
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    @Override
    public void play(float volume) {
        soundPool.play(soundId, volume, volume, 0, 0, 1); //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        //int: loop mode (0 = no loop, -1 = loop forever)
        // Play a sound from a sound ID.rate is playback rate播放速率
    }

    @Override
    public void dispose() {
        soundPool.unload(soundId); //dispose this soundId
    }

}
