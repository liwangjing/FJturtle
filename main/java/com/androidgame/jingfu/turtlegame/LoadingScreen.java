package com.androidgame.jingfu.turtlegame;

import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Input;
import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;

import java.util.List;

/**
 * Created by handsomemark on 6/28/16.
 */
public class LoadingScreen extends Screen {
    private int count = 0;
    private int lastingTime = 0;
    private static final float DISPLAYDURATION = 40; //1s
    private int fruitW;//90
    private int fruitH;//90
    private int offsetX; //90
    private int fruitX ;//
    private int fruitY ;
    private int snakeX ;
    private int snakeY ;
    private boolean hasUpdated = false;

    public LoadingScreen(Game game) {
        super(game);
        Assets.click = game.getAudio().createSound("click.wav");
        Graphics g = game.getGraphics();
        Assets.fruit = g.newImage("fruit.png", Graphics.ImageFormat.RGB565);
        Assets.snake = g.newImage("snake.png", Graphics.ImageFormat.RGB565);
        this.fruitW = Assets.fruit.getWidth(); //90
        this.fruitH = Assets.fruit.getHeight(); //90
        this.offsetX = fruitW ; //90
        this.fruitX = AndroidGame.PORTRAITHEIGHT/2 - 3*offsetX;//370=640-270
        this.fruitY = AndroidGame.PORTRAITWIDTH/2 - offsetX/2;//315
    }

    @Override
    public void update(float deltaTime) {

        lastingTime += deltaTime;
        if (count < 4) {
            if (lastingTime > DISPLAYDURATION-10) {
                snakeX = AndroidGame.PORTRAITHEIGHT/2-offsetX*count+5;
                snakeY = fruitY;
                count++;
                lastingTime = 0;
                hasUpdated = true;
            }
        } else if (count == 4) {
            if (lastingTime > DISPLAYDURATION-20) {
                snakeX = fruitX;
                count++;
                lastingTime = 0;
                hasUpdated = true;
            }
        }
        else if (count > 4){
            if (lastingTime > DISPLAYDURATION/7) {
                snakeX = fruitX - (offsetX/2) * (count-3);
                count++;
                lastingTime = 0;
                hasUpdated = true;
            }
        }

        if( snakeX < -200) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void paint(float deltaTime) {
        if (hasUpdated) {
            Graphics g = game.getGraphics();
            //g.drawARGB(255, 0, 0, 0);//全黑，完全不透明的底面
            g.drawARGB(255,0,0,0);
            if (count < 4) {
                g.drawScaledImage(Assets.fruit, fruitX, fruitY, offsetX, offsetX, 0, 0, fruitW, fruitH);
                g.drawScaledImage(Assets.snake, snakeX, snakeY, Assets.snake.getWidth() , offsetX, 0, 0, Assets.snake.getWidth(), Assets.snake.getHeight());
                hasUpdated = false;
            } else if (count == 4) {
                g.drawScaledImage(Assets.snake, fruitX, snakeY, Assets.snake.getWidth() , offsetX, 0, 0, Assets.snake.getWidth(), Assets.snake.getHeight());
                hasUpdated = false;
            } else if (count > 4) {
                g.drawScaledImage(Assets.snake, snakeX, snakeY, Assets.snake.getWidth() , offsetX, 0, 0, Assets.snake.getWidth(), Assets.snake.getHeight());
                hasUpdated = false;
            }
            //g.drawImage(Assets.splash, 0, 0);
        }
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
