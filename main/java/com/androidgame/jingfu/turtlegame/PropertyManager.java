package com.androidgame.jingfu.turtlegame;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.Game;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by handsomemark on 6/30/16.
 */
public class PropertyManager {

    private Context context;
    private static Properties props = new Properties();
    private static PropertyManager uniqueInstance;

    private PropertyManager(Context context) {
        this.context = context;
        try {
            props.load(context.getAssets().open("FJturtle.properties"));
        }
        catch (IOException err) {
            err.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static synchronized PropertyManager getInstance(Context context) {
        if (uniqueInstance == null)
            uniqueInstance = new PropertyManager(context);
        return uniqueInstance;
    }
}
