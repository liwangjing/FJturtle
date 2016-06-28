package com.androidgame.jingfu.fjturtle.framework;

/**
 * Created by handsomemark on 6/14/16.
 */
public interface Game {
    public Audio getAudio();
    public Input getInput();
    public FileIO getFileIO();
    public Graphics getGraphics();
    public void setScreen(Screen screen);
    public Screen getCurrentScreen();
    public Screen getInitScreen();
}
