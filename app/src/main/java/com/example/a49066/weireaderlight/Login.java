package com.example.a49066.weireaderlight;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends Activity {

    WebView web;
    SharedPreferences sharepre;
    SharedPreferences.Editor editor;
    URL loginurl,getTokenurl;
    Handler handle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        sharepre=getSharedPreferences("important_information",MODE_PRIVATE);
        editor=sharepre.edit();

        web=(WebView)findViewById(R.id.login_webview);
        web.getSettings().setJavaScriptEnabled(true);

        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                startActivity(new Intent(Login.this,MainPage.class));
                finish();
            }
        };


        if(sharepre.getString("access_token",null)!=null){
            startActivity(new Intent(Login.this,MainPage.class));
            finish();
        }

        try{
            loginurl=new URL("https://open.weibo.cn/oauth2/authorize?"+
                    "client_id=1493954399"+
                    "&redirect_uri=https://api.weibo.com/oauth2/default.html"+
                    "&scope=all"+
                    "&display=mobile"+
                    "&response_type=code");
        }catch (Exception e){}

        web.loadUrl(loginurl.toString());

        web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,String url){
                Uri uri=Uri.parse(url);
                String code= uri.getQueryParameter("code");
                try {
                    getTokenurl = new URL("https://api.weibo.com/oauth2/access_token?"+
                            "&client_id=1493954399"+
                            "&client_secret=370e5ba18f46f5bad978afaabf703eac"+
                            "&grant_type=authorization_code"+
                            "&code="+code+
                            "&redirect_uri=https://api.weibo.com/oauth2/default.html");
                    System.out.println("here0");
                    new Thread(){
                        @Override
                        public void run(){
                            try {
                                HttpURLConnection conn = (HttpURLConnection) getTokenurl.openConnection();
                                conn.setRequestMethod("POST");
                                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                handle.sendMessage(new Message());
                                JSONObject obj=JSONObject.fromObject(reader.readLine());
                                editor.putString("access_token",obj.getString("access_token"));
                                editor.commit();
                            }catch (Exception e){}
                        }
                    }.start();
                }catch (Exception e){}

                return true;
            }
        });
    }
}

