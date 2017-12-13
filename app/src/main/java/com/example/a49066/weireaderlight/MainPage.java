package com.example.a49066.weireaderlight;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainPage extends Activity {
    ViewPager viewpager;
    FloatingActionButton fab;
    ArrayList<View> viewList;
    PagerAdapter pageradapter;
    SharedPreferences sharepre;
    SharedPreferences.Editor editor;
    Handler handle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mainpage);

        viewList=new ArrayList<>();
        viewpager=(ViewPager)findViewById(R.id.mainpage_viewpager);
        fab=(FloatingActionButton)findViewById(R.id.mainpage_fab);
        sharepre=getSharedPreferences("important_information",MODE_PRIVATE);
        editor=sharepre.edit();
        //viewpager
        pageradapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object){
                container.removeView(viewList.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }

            @Override
            public int getItemPosition(Object object){
                return POSITION_NONE;
            }
        };
        viewpager.setAdapter(pageradapter);

        Internet.internetThreadPool= Executors.newFixedThreadPool(30);
        ViewPagerLoad.ViewThreadPool= Executors.newFixedThreadPool(30);
        BitmapLoader.BitmapThreadPool= Executors.newFixedThreadPool(30);

        new Thread(){
            public void run() {
                try {
                    String urlString = "https://api.weibo.com/2/statuses/home_timeline.json?"
                            + "access_token=" + sharepre.getString("access_token", "")
                            + "&count=20";
                    Future<BufferedReader> future = Internet.internetThreadPool.submit(new Internet(urlString, "GET"));
                    JSONObject obj = JSONObject.fromObject(future.get().readLine());
                    JSONArray array = obj.getJSONArray("statuses");
                    Future<View>[] futureView=new Future[array.size()+1];
                    for (int i = 0; i < array.size(); i++)
                        futureView[i]=ViewPagerLoad.ViewThreadPool.submit(new ViewPagerLoad(array.getJSONObject(i),getLayoutInflater().inflate(R.layout.inforpage,null),MainPage.this));
                    for (int i = 0; i < array.size(); i++)
                        try{
                            viewList.add(futureView[i].get());
                        }catch (Exception e){}
                    Message msg=Message.obtain();
                    msg.what=2;
                    handle.sendMessage(msg);
                }catch (Exception e){}
                Message msg=Message.obtain();
                msg.what=1;
                handle.sendMessage(msg);
            }
        }.start();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setClickable(false);
                new Thread(){
                    public void run() {
                        try {
                            String urlString = "https://api.weibo.com/2/statuses/home_timeline.json?"
                                    + "access_token=" + sharepre.getString("access_token", "")
                                    + "&count=20";
                            Future<BufferedReader> future = Internet.internetThreadPool.submit(new Internet(urlString, "GET"));
                            JSONObject obj = JSONObject.fromObject(future.get().readLine());
                            JSONArray array = obj.getJSONArray("statuses");
                            Future<View>[] futureView=new Future[array.size()+1];
                            for (int i = 0; i < array.size(); i++)
                                futureView[i]=ViewPagerLoad.ViewThreadPool.submit(new ViewPagerLoad(array.getJSONObject(i),getLayoutInflater().inflate(R.layout.inforpage,null),MainPage.this));
                            for (int i = 0; i < array.size(); i++)
                                try{
                                    viewList.add(futureView[i].get());
                                }catch (Exception e){}
                            Message msg=Message.obtain();
                            msg.what=2;
                            handle.sendMessage(msg);
                        }catch (Exception e){}
                        Message msg=Message.obtain();
                        msg.what=1;
                        handle.sendMessage(msg);
                    }
                }.start();
            }
        });

        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==1)
                    fab.setClickable(true);
                else if(msg.what==2) {
                    pageradapter.notifyDataSetChanged();
                }
            }
        };
    }
}
