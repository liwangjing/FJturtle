package com.androidgame.jingfu.turtlegame;


import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidImage;

/**
 * Created by handsomemark on 6/28/16.
 */
public class FJAdScreen extends Screen {

    private static final float DISPLAYDURATION = 100; // 2.00 sec

    private float lastingTime = 0;


    public FJAdScreen(Game game) {
        super(game);
        Graphics g = game.getGraphics();
        Assets.fjad = g.newImage("fjad.jpg", Graphics.ImageFormat.ARGB8888);
    }

    @Override
    public void update(float deltaTime) {
        lastingTime += deltaTime;
        if (lastingTime > DISPLAYDURATION) {
             /*if current lasting time of this ad. screen is larger than the prefixed duration, than
             change the screen to loading screen*/
            game.setScreen(new LoadingScreen(game));
            lastingTime = 0;
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.fjad,0,0);
//        g.drawScaledImage(Assets.fjad, 0 , 0 , GameActivity.PORTRAITHEIGHT, GameActivity.PORTRAITWIDTH, 0, 0, Assets.fjad.getWidth(), Assets.fjad.getHeight());
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
