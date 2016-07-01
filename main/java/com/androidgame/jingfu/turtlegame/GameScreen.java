package com.androidgame.jingfu.turtlegame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsomemark on 6/29/16.
 */
public class GameScreen extends Screen {

    public  static final int rows = Integer.parseInt(PropertyManager.getProperty("rows"));
    public  static final int columns = Integer.parseInt(PropertyManager.getProperty("columns"));

    private List<Led> leds= new ArrayList<Led>();


    public GameScreen(Game game) {
        super(game);
        // generate 61 Leds and assign its game cooedinates(i,j) and screen coordinates(380+(j-1)*65),(210-(j-1)*37+(i-1)*75)
        for (int i=1;i<=rows ;i++ ) {
            for (int j=1; j<=columns ; j++) {
                if (Math.abs(i-j)>=((columns+1)/2)) {
                    continue;//do not creat led object with coordinates difference larger or equal than 5
                }
                leds.add(new Led((380+(j-1)*65),(210-(j-1)*37+(i-1)*75),i,j));
            }
        }
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        // paint hexagon
        // set outside coordinates and paint the outside hexagon
        int[] px={ 640, 947, 947, 640, 333, 333};
        int[] py={ 5, 182, 537,715, 537, 182};
        fillHexagon( px, py, g, Color.RED);
        // set inside coordinates and paint the inside hexagon
        px = new int[]{ 640, 938, 938, 640, 341, 341};
        py = new int[]{ 15, 187, 532, 705, 532, 187};
        fillHexagon( px, py, g, Color.BLACK);
        // paint LEDs
        for (int i=0;i<leds.size() ;i++ ) {
            leds.get(i).draw(g);
        }
    }


    private void fillHexagon(int[] px, int[] py, Graphics g, int color) {
        Paint p = new Paint();
        p.setColor(color);
        p.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(px[0], py[0]);
        for (int i = 1; i < px.length; i++) {
            path.lineTo(px[i], py[i]);
        }
        path.close();
        g.drawPath(path,p);
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
