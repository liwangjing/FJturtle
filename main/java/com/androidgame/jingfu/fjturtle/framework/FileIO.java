package com.androidgame.jingfu.fjturtle.framework;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by handsomemark on 6/14/16.
 */
public interface FileIO {

    public InputStream readFile(String file) throws IOException; // input file name, read this file, return the input stream of bytes.

    public OutputStream writeFile(String file) throws IOException; //input filename, write this file, return the

    public InputStream readAsset(String file) throws IOException;

    public AssetManager getAssets() ;

    public SharedPreferences getSharedPref(); // SharedPreferences: Android interface letting us access & modify preference

    public void putIntegerToPref(String key, int num);

    public int getIntegerFromPref(String key, int defValue);

    public void removeFromPref(String key);
}
