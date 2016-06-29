package com.androidgame.jingfu.fjturtle.framework.implementation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;


import com.androidgame.jingfu.fjturtle.framework.Graphics;
import com.androidgame.jingfu.fjturtle.framework.Image;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by handsomemark on 6/14/16.
 */
public class AndroidGraphics implements Graphics {

    AssetManager assets;
    Bitmap frameBuffer;
    Canvas canvas; // where we draw images onto and it appears on the screen
    Paint paint;


    public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer; //从外部传入一个可作画区域
        this.canvas = new Canvas(frameBuffer);  //从这个可作画区域获取一个画布canvas
        this.paint = new Paint(); //paint用于规定作画方式以及作画颜色
    }

    @Override
    public Image newImage(String fileName, ImageFormat format) {
        Config config = null;
        if (format == ImageFormat.RGB565)
            config = Config.RGB_565;
        else if (format == ImageFormat.ARGB4444)
            config = Config.ARGB_4444;
        else
            config = Config.ARGB_8888;

        Options options = new Options();  //this option is used for the following decodeStream();
        options.inPreferredConfig = config;  //set the config of option
        InputStream in =null;
        Bitmap bitmap = null;
        try {
            in = assets.open(fileName); // assetManager 去 assets 中读取一个file，return 一个inputStream
            bitmap = BitmapFactory.decodeStream(in, null, options); //通过BitmapFactory的decodeStream()方法，来解读从文件中读到的input stream.
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset'" + fileName + "'");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset'" + fileName + "'");
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    // close the input stream at the end anyway!
                }
            }
        }
        if (bitmap.getConfig() == Config.RGB_565)
            format = ImageFormat.RGB565;
        else if (bitmap.getConfig() == Config.ARGB_4444)
            format = ImageFormat.ARGB4444;
        else
            format = ImageFormat.ARGB8888;
        return new AndroidImage(bitmap, format); //返回一个 AndroidImage 类型的instance。
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawColor(color);
    }

    @Override
    public void drawLine(int x, int y, int x2, int y2, int color) {
        paint.setColor(color);
        canvas.drawLine(x,y,x2,y2,paint);
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Style.FILL);
        canvas.drawRect(x, y, x+width-1, y+height-1, paint);
    }

    @Override
    public void drawCircle() {

    }

    @Override
    public void drawImage(Image image, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) {
        Rect srcRect = new Rect();
        Rect dstRect = new Rect();

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth;
        dstRect.bottom = y + srcHeight;
        canvas.drawBitmap(((AndroidImage)image).bitmap, srcRect, dstRect, null);
    }

    @Override
    public void drawImage(Image image, int x, int y) {
        canvas.drawBitmap(((AndroidImage)image).bitmap, x, y, null );
    }

    @Override
    public void drawString(String text, int x, int y, Paint paint) {
        canvas.drawText(text, x, y, paint);
    }

    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    public void drawScaledImage(Image image, int x, int y, int width, int height, int srcX, int srcY, int srcWidth, int srcHeight) {
        Rect srcRect = new Rect();
        Rect dstRect = new Rect();

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth;
        srcRect.bottom = srcY + srcHeight;

        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + width;
        dstRect.bottom = y + height;
        canvas.drawBitmap(((AndroidImage)image).bitmap, srcRect, dstRect, null);
    }
}
