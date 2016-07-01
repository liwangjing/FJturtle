package com.androidgame.jingfu.turtlegame;

import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Image;
import com.androidgame.jingfu.fjturtle.framework.Input;
import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;
import com.androidgame.jingfu.fjturtle.framework.implementation.MultiTouchHandler;

import java.util.List;

/**
 * Created by jing on 2016/6/30.
 */
public class MenuScreen extends Screen {

    public MenuScreen (Game game){
        super(game);
        game.getInput().getTouchEvents();//clear touch events
    }
    @Override
    public void update(float deltaTime) {
        if (game.getInput().getPointerCounter() == 0) {
            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);
                if (event.type == Input.TouchEvent.TOUCH_UP && inBounds(event, ((AndroidGame) game).landscapeWidth * 104 / Assets.menu.getWidth(), ((AndroidGame) game).landscapeHeight * 680 / Assets.menu.getHeight(),
                        ((AndroidGame) game).landscapeWidth * 468 / Assets.menu.getWidth(), ((AndroidGame) game).landscapeHeight * 212 / Assets.menu.getHeight()) ) {
                    // once find the user has lifted his finger from the play button area, start game screen
                    game.setScreen(new GameScreen(game));
                    break;// since the touchEvents are cleared again in setScreen(), so don't traverse the old events list. Otherwise, the program will break down.
                }
            }
        }
    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawScaledImage(Assets.menu, 0, 0, ((AndroidGame)game).landscapeWidth, ((AndroidGame)game).landscapeHeight, 0, 0, Assets.menu.getWidth(), Assets.menu.getHeight());
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
