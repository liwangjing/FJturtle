package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.androidgame.jingfu.fjturtle.framework.Audio;
import com.androidgame.jingfu.fjturtle.framework.FileIO;
import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Input;
import com.androidgame.jingfu.fjturtle.framework.Screen;


/**
 * Created by handsomemark on 6/14/16.
 */
public abstract class AndroidGame extends Activity implements Game {
    AndroidFastRenderView renderView;
    Graphics graphics;
    Audio audio;
    Input input;
    FileIO fileIO;
    Screen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //Window.FEATURE_NO_TITLE:flag for 'no title' feature, turn off the title at the top of screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*
        *  requestWindowFeature() = getWindow.requestFeature();
        *  WindowManager.LayoutParams.FLAG_FULLSCREEN： hide all screen decorations while this window is displayed
        * */

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT; //device is in vertical orientation
        /*
        * getResources() is the method of context, it returns a resource instance for the app's package
        * Resource used for accessing the app's resources, is on top of assets manager.
        * getConfiguration(): returns the current configuration that is effect for this resource object.
        * orientation is the orientation of screen, in this case, it is set to be landscape.ORIENTATION_PORTRAIT / ORIENTATION_LANDSCAPE
        * */
        int frameBufferWidth = isPortrait? 720 : 1280;
        int frameBufferHeight = isPortrait? 1280 : 720;
        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);

        float scaleX = (float) frameBufferWidth / getResources().getDisplayMetrics().widthPixels;
        float scaleY = (float) frameBufferHeight / getResources().getDisplayMetrics().heightPixels;
        // getDisplayMetrics()的长与高，是随屏幕的orientation而变化的。永远认为屏幕的水平的边的长度为长。
        // 当屏幕旋转时，会销毁当前activity（call onDestroy()，再call onCreate()方法）。

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen(); // Game interface 中的方法。空方法，因为现在是抽象类中，所以可以不必实现。
        setContentView(renderView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        screen.resume();
        renderView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        renderView.pause();
        screen.pause();
        if (isFinishing()) screen.dispose(); // isFinishing comes from Activity
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null!");
        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }

}
