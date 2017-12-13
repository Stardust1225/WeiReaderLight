package com.example.a49066.weireaderlight;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by 49066 on 2017/12/12.
 */

public class BitmapLoader implements Callable<Bitmap> {
    String urlString;
    static ExecutorService BitmapThreadPool;
    public BitmapLoader(String s){
        this.urlString=new String(s);
    }
    @Override
    public Bitmap call() throws Exception {
        Bitmap bmp=null;
        try{
            URL url=new URL(urlString);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(8000);
            bmp= BitmapFactory.decodeStream(conn.getInputStream());
        }catch (Exception e){}
        return  bmp;
    }
}
