package com.androidgame.jingfu.turtlegame;

import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Input;
import com.androidgame.jingfu.fjturtle.framework.Screen;

import java.util.List;

/**
 * Created by handsomemark on 6/28/16.
 */
public class LoadingScreen extends Screen {

    private float lastingTime = 0;
    private static final float DISPLAYDURATION = 150; // duration of ad screen is 1.50 sec

    public LoadingScreen(Game game) {
        super(game);
        Assets.click = game.getAudio().createSound("click.wav");
    }

    @Override
    public void update(float deltaTime) {
        lastingTime += deltaTime;
        if (lastingTime > DISPLAYDURATION) {
             /*if current lasting time of this ad. screen is larger than the prefixed duration, than
             change the screen to loading screen*/
            game.setScreen(new GameScreen(game));
            lastingTime = 0;
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawARGB(155,0,0,0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}
