package com.androidgame.jingfu.turtlegame;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.FileIO;
import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Input.TouchEvent;
import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by handsomemark on 6/29/16.
 */
public class GameScreen extends Screen {
    enum GameState {
        WARMUP,READY,RUNNING,PAUSE,GAMEOVER
    }
    GameState state = GameState.WARMUP;

    public  static final int rows = Integer.parseInt(PropertyManager.getProperty("rows"));
    public  static final int columns = Integer.parseInt(PropertyManager.getProperty("columns"));

    public List<Led> leds= new ArrayList<Led>();

    public Snake mySnake = new Snake(this);
    public Fruit fruit = new Fruit(this);

    private int warmUpFlag = 0;
    private float lastTime = 0;
    int[] ledsX = {1,2,3,4,5,6,7,8,9,9,9,9,9,8,7,6,5,4,3,2,1,1,1,1,1};
    int[] ledsY = {5,6,7,8,9,9,9,9,9,8,7,6,5,4,3,2,1,1,1,1,1,2,3,4,5};

    Paint scorepaint,paint2;


    public GameScreen(Game game) {
        super(game);
        game.getInput().getTouchEvents();// clear event
        // generate 61 Leds and assign its game coordinates(i,j) and screen coordinates(380+(j-1)*65),(210-(j-1)*37+(i-1)*75)
        for (int i=1;i<=rows ;i++ ) {
            for (int j=1; j<=columns ; j++) {
                if (Math.abs(i-j)>=((columns+1)/2)) {
                    continue;//do not create led object with coordinates difference larger or equal than 5
                }
                leds.add(new Led((380+(j-1)*65),(210-(j-1)*37+(i-1)*75),i,j));
            }
        } // obtain led instances in constructor
        this.scorepaint = new Paint();
        scorepaint.setTextSize(70);
        scorepaint.setTextAlign(Paint.Align.CENTER);
        scorepaint.setAntiAlias(true);
        scorepaint.setColor(Color.WHITE);
        this.paint2 = new Paint();
        paint2.setTextSize(90);
        paint2.setTextAlign(Paint.Align.LEFT);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

        if (state == GameState.WARMUP){
            updateWarmUp(deltaTime);
        }
        if (state == GameState.READY) {
            updateReady(touchEvents);
        }
        if (state == GameState.RUNNING) {
            updateRunning(touchEvents);
        }
        if (state == GameState.PAUSE){
            updatePause(touchEvents);
        }
        if (state == GameState.GAMEOVER) {
            updateGameOver(deltaTime);
        }

    }


    @Override
    public void paint(float deltaTime) {
        paintFrame();
        Graphics g = game.getGraphics();
        if (state != GameState.WARMUP) {
            mySnake.draw(g);
            fruit.draw(g);
        }
        if (state == GameState.WARMUP){
            drawWarmUpUI();
        }
        if (state == GameState.READY) {
            drawReadyUI();
        }
        if (state == GameState.RUNNING) {
            drawRunningUI();
        }
        if (state == GameState.PAUSE){
            drawPauseUI();
        }
        if (state == GameState.GAMEOVER) {
            drawGameOverUI();
        }
    }

    /*
    * draw the internal and external boundaries of the hexagon
    * draw the led circles.
    * */
    private void paintFrame() {
        Graphics g = game.getGraphics();
        g.drawARGB(255, 0, 0, 0);
        // paint hexagon
        // set outside coordinates and paint the outside hexagon
        int[] px={ 640, 947, 947, 640, 333, 333};
        int[] py={ 5, 182, 537,715, 537, 182};
        fillHexagon( px, py, g, Color.RED);
        // set inside coordinates and paint the inside hexagon
        px = new int[]{ 640, 938, 938, 640, 341, 341};
        py = new int[]{ 15, 187, 532, 705, 532, 187};
        fillHexagon(px, py, g, Color.BLACK);
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

    private void updateWarmUp(float deltaTime) {
        lastTime = deltaTime + lastTime;
        if (lastTime>5) {
            warmUpFlag++;
            lastTime = 0;
            if (warmUpFlag > 24 ) {
                warmUpFlag = 0;
                state = GameState.READY;
            }
        }
    }

    private void updateReady(List<TouchEvent> touchEvents){
        if (touchEvents.size()>0){
            state = GameState.RUNNING;
        }
    }

    private void updateRunning(List<TouchEvent> touchEvents){
        if (!mySnake.getLive()){
            state = GameState.GAMEOVER;
        } else {
            int length = touchEvents.size();
            for (int i = 0; i < length; i++) {
                TouchEvent event = touchEvents.get(i);
                if (event.type == TouchEvent.TOUCH_DOWN) {
                    if (inBounds(event, 0, 0, ((AndroidGame) game).landscapeWidth / 2, ((AndroidGame) game).landscapeHeight)) {
                        mySnake.setbL(true);
                        mySnake.L++;
                    }
                    if (inBounds(event, ((AndroidGame) game).landscapeWidth / 2, 0, ((AndroidGame) game).landscapeWidth / 2, ((AndroidGame) game).landscapeHeight)) {
                        mySnake.setbR(true);
                        mySnake.R++;
                    }
                    mySnake.determineDirection();
                }
                if (event.type == TouchEvent.TOUCH_UP) {
                    if (inBounds(event, 0, 0, ((AndroidGame) game).landscapeWidth / 2, ((AndroidGame) game).landscapeHeight)) {
                        mySnake.setbL(false);
                    }
                    if (inBounds(event, ((AndroidGame) game).landscapeWidth / 2, 0, ((AndroidGame) game).landscapeWidth / 2, ((AndroidGame) game).landscapeHeight)) {
                        mySnake.setbR(false);
                    }
                }
            }
            if (length > 0 && mySnake.isStopped()) {
                mySnake.setStopped(false);
            }
            mySnake.snakeGetFruit(fruit);
        }
    }

    private void updatePause(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 0, (int) (0.4 * ((AndroidGame) game).landscapeHeight)-90, (int)( ((AndroidGame) game).landscapeWidth *0.45),90 )  ) {
                    resume();
                }
                if (inBounds(event, 0, (int)(0.7 * ((AndroidGame) game).landscapeHeight)-90, (int)( ((AndroidGame) game).landscapeWidth*0.4), 90)) {
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(float deltaTime){
        lastTime = lastTime + deltaTime;
        if (lastTime > 50) {
            int score = mySnake.getNumOfFruits();
            saveScore(score); //save the score to rank.
        nullify();
        lastTime = 0;
        game.setScreen(new MenuScreen(game));
        }
    }

    private void drawWarmUpUI(){
        Graphics g = game.getGraphics();
            for(int j = 0; j < leds.size();j++){
                if(leds.get(j).getX() == ledsX[warmUpFlag] && leds.get(j).getY() == ledsY[warmUpFlag]){
                    leds.get(j).draw(g,Brightness.H);
                }
            }
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();
        g.drawString("0", (int) (0.15 * ((AndroidGame) game).landscapeWidth), (int) (0.14 * ((AndroidGame) game).landscapeHeight), scorepaint);
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        g.drawString(""+mySnake.getNumOfFruits(), (int) (0.15 * ((AndroidGame) game).landscapeWidth), (int) (0.14 * ((AndroidGame) game).landscapeHeight), scorepaint);
    }

    private void drawPauseUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);// Darken the screen
        g.drawString("Resume", (int) (0.0 * ((AndroidGame) game).landscapeWidth), (int) (0.40 * ((AndroidGame) game).landscapeHeight), paint2);// 0.5 0.23
        g.drawString("Menu", (int)(0.0*((AndroidGame) game).landscapeWidth), (int)(0.70 * ((AndroidGame) game).landscapeHeight), paint2); // 0.5 0.5
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawString(""+mySnake.getNumOfFruits(), (int) (0.15 * ((AndroidGame) game).landscapeWidth), (int) (0.14 * ((AndroidGame) game).landscapeHeight), scorepaint);
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    @Override
    public void pause() {
        if (state == GameState.RUNNING || state == GameState.READY) {
            state = GameState.PAUSE;
            mySnake.setStopped(true);
        }
    }

    @Override
    public void resume() {
        if (state == GameState.PAUSE) {
            state = GameState.RUNNING;
        }
    }

    private void saveScore(int score){
        if (score <= 0) return; // only save score that is higher than 0.
        FileIO fileIO = game.getFileIO();
        int first = fileIO.getIntegerFromPref("1st", -1);
        int second = fileIO.getIntegerFromPref("2nd", -2);
        int third = fileIO.getIntegerFromPref("3rd", -3);
        if (score == first || score == second || score == third) return; // discard duplicate score.

        if (score >= second){
            if (score >= first ){ //first has value and score higher than first
                fileIO.putIntegerToPref("1st", score);
                if (first >=0){
                    fileIO.putIntegerToPref("2nd", first);
                    if (second>=0) { // second has value, then move"first" to "2nd".
                        fileIO.putIntegerToPref("3rd", second);
                    }
                }
            } else { // second =< score < first
                fileIO.putIntegerToPref("2nd", score);
                if (second > 0){ // "2nd" has value, give to "3rd"
                    fileIO.putIntegerToPref("3rd", second);
                }
            }
        } else { // score < second
            if (score > third ) { //third =< score < second
                fileIO.putIntegerToPref("3rd",score);
            }
        }
    }

    private void nullify() {
        // Set all variables to null. You will be recreating them in the
        // constructor.
        scorepaint = null;
        paint2 = null;
        leds=null;
        System.gc();// Call garbage collector to clean up memory.
    }
    
    private void goToMenu () {
        game.setScreen(new MenuScreen(game,this));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
            pause();
    }
}
