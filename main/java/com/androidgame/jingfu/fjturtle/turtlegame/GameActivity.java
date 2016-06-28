package com.androidgame.jingfu.fjturtle.turtlegame;

import android.util.Log;

import com.androidgame.jingfu.fjturtle.framework.Screen;
import com.androidgame.jingfu.fjturtle.framework.implementation.AndroidGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jing on 2016/6/28.
 */
public class GameActivity extends AndroidGame {

        public static String map;
        boolean firstTimeCreate = true;

        @Override
        public Screen getInitScreen() {
            return null;
        }

        private static String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append((line+"\n"));
                }
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.w("LOG", e.getMessage());
                }
            }
            return stringBuilder.toString();
        }

        @Override
        public void onBackPressed() {
            getCurrentScreen().backButton();
        }

        @Override
        protected void onResume() {
            super.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
        }

        @Override
        protected void onStop() {
            super.onStop();
        }
    }
