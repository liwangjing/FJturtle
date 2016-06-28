package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;


import com.androidgame.jingfu.fjturtle.framework.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by handsomemark on 6/14/16.
 */
public class AndroidFileIO implements FileIO {

    Context context;
    AssetManager assets;
    String externalStoragePath;

    public AndroidFileIO(Context context) {
        this.context = context;
        this.assets = context.getAssets(); // get a assetManager from current context.
        this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        /*
        * environment: provide access to environment variables, like storage statement, storage directory...
        * getExternalStorageDirectory() return a primary/external storage directory,needs permission.
        *   this directory is a media/shared directory that can store relatively large data and is shared across all apps.
        * getAbsolutePath() returns the absolute path(a path that starts at a root of the file system) of the file.
        * File.separator is a string, system-depend default name separator, only a 'separatechar' in it, like'/' in UNIX.
         */
    }

    @Override
    public InputStream readFile(String file) throws IOException {
        return new FileInputStream(externalStoragePath + file); //opening a connection to an actual file, obtain input bytes from the file.
    }

    @Override
    public OutputStream writeFile(String file) throws IOException {
        return new FileOutputStream(externalStoragePath + file);
    }

    @Override
    public InputStream readAsset(String file) throws IOException {
        return assets.open(file); //open an asset using ACCESS_STREAMING mode, only the file that is bundled to the app as asset.
        // ACCESS_STREAMING: read sequentially with an occasional forward seek.
    }

    @Override
    public SharedPreferences getSharedPref() {
        return PreferenceManager.getDefaultSharedPreferences(context);
        /*
        * sharedPreferences is an interface for accessing and modifying preference data.
        *   for any particular sets of preferences, there is a single instance of this class shared by all clients.
        *   modify preferences through editor[get editor from edit();]; and any change saved through commit();
        * PreferenceManager is a class that used for create preference hierarchies from activities or xml.
        * getDefaultSharedPreferences(context): gets a SharedPreference instance that points to the default file in the given context.
         */
    }
}
