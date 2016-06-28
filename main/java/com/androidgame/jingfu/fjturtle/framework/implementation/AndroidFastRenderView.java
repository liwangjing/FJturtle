package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by handsomemark on 6/14/16.
 * This class creates a SurfaceView which you can use to create graphics-based UI and update quickly
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable{
    /*
    * Runnable: class whose instances are intended to be executed by a thread. this class must have run();
    *
    * */

    AndroidGame game;
    Bitmap frameBuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;

    public AndroidFastRenderView(AndroidGame game, Bitmap frameBuffer) {
        super(game);
        this.game = game;
        this.frameBuffer = frameBuffer;
        this.holder = getHolder();
        /*
        * getHolder() returns a SurfaceHolder, SH is an interface, it provides access and control over Surface's underlying surface.
         * it lets us to control the size and format of the surface,
         * edit the pixel in the surface. SH is available in SurfaceView class.
        * */
    }


    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void run() {
        Rect dstRect = new Rect(); // create a new empty rectangular.
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid()) continue;  // getSurface() returns a surface object, if its not valid, then wait;
            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f; // frame rate independent movement time arg
            startTime = System.nanoTime();
            if (deltaTime > 3.15) deltaTime = (float) 3.15; //0.0315sec
            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);
            // double buffer。
            Canvas canvas = holder.lockCanvas(); // start editing the surface
            canvas.getClipBounds(dstRect); //
            canvas.drawBitmap(frameBuffer, null, dstRect, null);//canvas.drawBitmap(bitmap, srcRect, dstRect, paint) 将bitmap上的srcRect区域画到成dstRect大小。
            holder.unlockCanvasAndPost(canvas); // finish editing,
        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join(); // wait for this thread to die.
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
