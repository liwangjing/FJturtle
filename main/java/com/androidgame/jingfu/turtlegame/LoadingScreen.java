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
    public LoadingScreen(Game game) {
        super(game);
        Assets.click = game.getAudio().createSound("click.wav");
    }

    @Override
    public void update(float deltaTime) {

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
