package com.androidgame.jingfu.turtlegame;


import android.os.Bundle;
import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;


/**
 * Created by jing on 2016/6/28.
 */
public class GameActivity extends AndroidGame {

    boolean firstTimeCreate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PropertyManager.getInstance(this); // new a property manager for the application
    }

    @Override
    public Screen getInitScreen() {
        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        }
        return new FJAdScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Assets.theme.play();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Assets.theme.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Assets.theme.stop();
    }

}
