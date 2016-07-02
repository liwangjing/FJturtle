package com.androidgame.jingfu.turtlegame;

import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jing on 2016/7/1.
 */
public class Snake {

    List<Integer> x = new ArrayList<Integer>() ;
    List<Integer> y = new ArrayList<Integer>() ;

    private int headX,headY;//store the position of snake head.
    private boolean bL = false, bR = false;
    private Direction dir ;
    public GameScreen gameScreen;
    private double snakeSpeed = Double.parseDouble(PropertyManager.getProperty("snakeSpeed"));
    private static Random r = new Random();
    Direction[] dirs = Direction.values();
    private int rn ;
    private boolean live = true;
    private int numOfFruits;
    private static int ending = 1;
    public int L=0,R=0;

    public Snake(int headX, int headY) {
        this.headX= headX;
        this.headY = headY;
        initializeSnake();
        new Thread(new MoveThread()).start();
    }

    public Snake(GameScreen screen) {
        this.gameScreen = screen;
        initializeSnake();
        new Thread(new MoveThread()).start();
    }


    public void draw(Graphics g){
        if (!live){
            if (ending > 13)return;
            if (ending%2==0) {

                for (int i=0; i<gameScreen.leds.size(); i++){
                    gameScreen.leds.get(i).draw(g);
                }
            }
            else {
                for (int i=0; i<gameScreen.leds.size(); i++){
                    for (int j=0; j<3; j++){
                        if(gameScreen.leds.get(i).getX() == x.get(j) && gameScreen.leds.get(i).getY() == y.get(j)){
                            switch (j) {
                                case 0:
                                    gameScreen.leds.get(i).draw(g,Brightness.H);
                                    break;
                                case 1:
                                    gameScreen.leds.get(i).draw(g,Brightness.M);
                                    break;
                                case 2:
                                    gameScreen.leds.get(i).draw(g,Brightness.L);
                                    break;
                            }
                        }
                    }
                }
            }
            ending++;
        } else {
            for (int j=0; j<3; j++){
                for (int i=0; i<gameScreen.leds.size(); i++){
                    if(gameScreen.leds.get(i).getX() == x.get(j) && gameScreen.leds.get(i).getY() == y.get(j)){
                        switch (j) {
                            case 0:
                                gameScreen.leds.get(i).draw(g,Brightness.H);
                                break;
                            case 1:
                                gameScreen.leds.get(i).draw(g,Brightness.M);
                                break;
                            case 2:
                                gameScreen.leds.get(i).draw(g,Brightness.L);
                                break;
                        }
                    }
                }
            }
        }
    }

    public  void move() {//move function only works on snake head.
        int tempX=headX;
        int tempY=headY;
        switch(dir){
            case U:
                headX = headX - 1;// y remain the same
                break;
            case RU:
                headY = headY + 1;//x remain the same
                break;
            case RD:
                headX = headX + 1;
                headY = headY + 1;
                break;
            case D:
                headX = headX + 1;
                break;
            case LD:
                headY = headY - 1;
                break;
            case LU:
                headX = headX - 1;
                headY = headY - 1;
                break;
            case STOP:
                return;
        }
        if((headX<1)||(headX>9)||(headY<1)||(headY>9)||((headX==1)&&(headY==6))||((headX==2)&&(headY==7))||((headX==3)&&(headY==8))||((headX==4)&&(headY==9))||((headX==9)&&(headY==4))||((headX==8)&&(headY==3))||((headX==7)&&(headY==2))||((headX==6)&&(headY==1))) {
            this.live = false;
            headX=tempX;
            headY=tempY;
            return;
        }//if the snake hit the boundary, then the snake is died.
        x.add(0,headX);
        x.remove(x.size() - 1);
        y.add(0, headY);
        y.remove(y.size() - 1);
        Assets.click.play(0.5f);
    }

    public  void determineDirection() {
        if(bL&&!bR) {//turn left
            rn=(6+rn-1)%6;
            dir=dirs[rn];
        }
        else  if (bR &&!bL) {//turn right
            rn=(6+rn+1)%6;
            dir=dirs[rn];
        }
    }

    public void initializeSnake() {
        do {
            headX=r.nextInt(4)+3;
            headY=r.nextInt(4)+3;
        } while (Math.abs(headX-headY) >=3);
        x.add(0,headX);
        y.add(0, headY);
        do {
            rn = r.nextInt(dirs.length);
            dir = dirs[rn];
        }while (dir==Direction.STOP);
        switch (dir) {
            case U:
                x.add(1,headX+1);
                y.add(1,headY);
                x.add(2,x.get(1)+1);
                y.add(2,y.get(1));
                break;
            case RU:
                x.add(1,headX);
                y.add(1,headY-1);
                x.add(2,x.get(1));
                y.add(2,y.get(1)-1);
                break;
            case RD:
                x.add(1,headX-1);
                y.add(1,headY-1);
                x.add(2,x.get(1)-1);
                y.add(2,y.get(1)-1);
                break;
            case D:
                x.add(1,headX-1);
                y.add(1,headY);
                x.add(2,x.get(1)-1);
                y.add(2,y.get(1));
                break;
            case LD:
                x.add(1,headX);
                y.add(1,headY+1);
                x.add(2,x.get(1));
                y.add(2,y.get(1)+1);
                break;
            case LU:
                x.add(1,headX+1);
                y.add(1,headY+1);
                x.add(2,x.get(1)+1);
                y.add(2,y.get(1)+1);
                break;
        }//switch closed
        dir = Direction.STOP;
    }

    public boolean snakeGetFruit(Fruit f) {
        if(f.getLive() && this.headX==f.getX() && this.headY==f.getY()) {
            f.setLive(false);
            this.numOfFruits++;
            Assets.eat.play(0.5f);
            if (numOfFruits<=Integer.parseInt(PropertyManager.getProperty("exponential"))) {
                snakeSpeed-=Math.sqrt(snakeSpeed);
            } else snakeSpeed-=Double.parseDouble(PropertyManager.getProperty("linearSpeed"));
            return true;
        }
        return false;
    }

    public int getNumOfFruits() {
        return numOfFruits;
    }

    public boolean getLive() {
        return live;
    }

    public double getSnakeSpeed() {
        return snakeSpeed;
    }

    public void setbL(boolean bl) {bL = bl;}
    public void setbR(boolean br) {bR = br;}

    public void setHeadX(int headX) {this.headX = headX;}
    public void setHeadY(int headY) {this.headY = headY;}

    public void setRn(int rn) {this.rn = rn; }
    public int getRn() {return rn; }

    private class MoveThread implements Runnable {//inner class
        private int lRn;
        public void run() {
            while (Snake.this.getLive()){
                double tempSpeed = Snake.this.getSnakeSpeed();
                if (Math.abs(Snake.this.L-Snake.this.R)>=3) {
                    if (Snake.this.L>Snake.this.R) {
                        Snake.this.rn=(6+lRn-2)%6;
                    } else {
                        Snake.this.rn=(6+lRn+2)%6;
                    }
                    Snake.this.dir=Snake.this.dirs[Snake.this.rn];
                }
                move();
                Snake.this.L=0;
                Snake.this.R=0;
                lRn=Snake.this.rn;
                try {
                    Thread.sleep(Integer.parseInt(new java.text.DecimalFormat("0").format(tempSpeed)));//time units milliseconds, period of refreshing the screen
                }
                catch (Exception err) {
                    System.err.println(err.getMessage());
                }
            }
        }
    }

}
