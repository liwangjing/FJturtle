package com.androidgame.jingfu.turtlegame;

import com.androidgame.jingfu.fjturtle.framework.Image;
import com.androidgame.jingfu.fjturtle.framework.Music;
import com.androidgame.jingfu.fjturtle.framework.Sound;

/**
 * Created by handsomemark on 6/28/16.
 */
public class Assets {
    public static Image menu, fjad,fruit,snake;
    public static Image button;
    public static Sound click,giggle,eat;
    public static Music theme;

    public static void load(GameActivity gameActivity) {
        theme = gameActivity.getAudio().createMusic("menutheme.mp3");
        theme.setLooping(true);
        theme.setVolume(0.85f); // at 85% volume
    }
}