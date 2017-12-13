package com.example.a49066.weireaderlight;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Created by 49066 on 2017/12/11.
 */

public class Internet implements Callable<BufferedReader>{
    static ExecutorService internetThreadPool;
    String urlString,method;
    public Internet(String urlstring,String method){
        this.urlString=new String(urlstring);
        this.method=new String(method);
    }

    @Override
    public BufferedReader call() throws Exception{
        BufferedReader reader=null;
        try{
            URL url=new URL(urlString);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(8000);
            reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }catch (Exception e){}
        return  reader;
    }
}
