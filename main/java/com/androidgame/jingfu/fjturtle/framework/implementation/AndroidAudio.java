package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;


import com.androidgame.jingfu.fjturtle.framework.Audio;
import com.androidgame.jingfu.fjturtle.framework.Music;
import com.androidgame.jingfu.fjturtle.framework.Sound;

import java.io.IOException;

/**
 * Created by handsomemark on 6/14/16.
 */
public class AndroidAudio implements Audio {

    AssetManager assets;
    SoundPool soundPool;

    public AndroidAudio(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC); //活动
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC,0);
    }

    @Override
    // 返回的对象，必须是继承Music这个接口的。
    public Music createMusic(String filename) { // longer music, read from file system each time
        try {
            AssetFileDescriptor assetFileDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetFileDescriptor);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    @Override
    public Sound createSound(String filename) { // short music
        try {
            AssetFileDescriptor assetFileDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}
