package com.example.a49066.weireaderlight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 49066 on 2017/12/12.
 */

public class ViewPagerLoad implements Callable<View> {
    JSONObject obj;
    View v;
    static ExecutorService ViewThreadPool;
    Context context;
    ImageView[] imageList;
    Pattern pattern1,pattern2,pattern3,pattern4;
    public ViewPagerLoad(JSONObject obj,View v,Context context){
                this.obj=JSONObject.fromObject(obj.toString());
        this.v=v;
        imageList=new ImageView[9];
        this.context=context;
    }

    @Override
    public View call() throws Exception {
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.inforpage_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MyAdapter());
        return recyclerView;
    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==1) {
                View v1=LayoutInflater.from(parent.getContext()).inflate(R.layout.inforhead,parent,false);
                try{
                    TextView name=(TextView) v1.findViewById(R.id.inforpage_name);
                    name.setText(obj.getJSONObject("user").getString("name"));
                    MyRoundImage image=(MyRoundImage) v1.findViewById(R.id.inforpage_head);
                    Future<Bitmap> future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(obj.getJSONObject("user").getString("profile_image_url")));
                    image.drawpicture(future.get());
                }catch (Exception e){}
                return new MyViewHolder(v1);
            }

            if(viewType==2){
                View v1=LayoutInflater.from(parent.getContext()).inflate(R.layout.inforcontent,parent,false);
                TextView content=(TextView)v1.findViewById(R.id.inforpage_content);
                content.setText(Html.fromHtml(textTranscation(obj.getString("text"))));
                return new MyViewHolder(v1);
            }

            if(viewType==3){
                View v1=LayoutInflater.from(parent.getContext()).inflate(R.layout.inforimage,parent,false);
                ImageView[] imageList=new ImageView[9];
                for(int i=0;i<9;i++){
                    imageList[i]=(ImageView)v1.findViewById(R.id.inforpage_image1+i);
                    imageList[i].setVisibility(View.GONE);
                }
                JSONArray thumbUrl=obj.getJSONArray("pic_urls");
                if(thumbUrl.size()==4){
                    try {
                        Future<Bitmap> future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(thumbUrl.getJSONObject(0).getString("thumbnail_pic")));
                        imageList[0].setVisibility(View.VISIBLE);
                        imageList[0].setImageBitmap(future.get());
                        imageList[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle=new Bundle();
                                bundle.putString("json",obj.toString());
                                bundle.putInt("position",0);
                                Intent intent=new Intent(context,ShowImage.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });

                        future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(thumbUrl.getJSONObject(1).getString("thumbnail_pic")));
                        imageList[1].setVisibility(View.VISIBLE);
                        imageList[1].setImageBitmap(future.get());
                        imageList[1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle=new Bundle();
                                bundle.putString("json",obj.toString());
                                bundle.putInt("position",1);
                                Intent intent=new Intent(context,ShowImage.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });

                        future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(thumbUrl.getJSONObject(2).getString("thumbnail_pic")));
                        imageList[3].setVisibility(View.VISIBLE);
                        imageList[3].setImageBitmap(future.get());
                        imageList[3].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle=new Bundle();
                                bundle.putString("json",obj.toString());
                                bundle.putInt("position",2);
                                Intent intent=new Intent(context,ShowImage.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });

                        future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(thumbUrl.getJSONObject(3).getString("thumbnail_pic")));
                        imageList[4].setVisibility(View.VISIBLE);
                        imageList[4].setImageBitmap(future.get());
                        imageList[4].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle=new Bundle();
                                bundle.putString("json",obj.toString());
                                bundle.putInt("position",3);
                                Intent intent=new Intent(context,ShowImage.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        });

                    }catch (Exception e){}
                }
                else {
                    final int start=imageList[0].getId();
                    for (int i = 0; i < thumbUrl.size(); i++)
                        try {
                            Future<Bitmap> future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(thumbUrl.getJSONObject(i).getString("thumbnail_pic")));
                            imageList[i].setVisibility(View.VISIBLE);
                            imageList[i].setImageBitmap(future.get());
                            imageList[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle=new Bundle();
                                    bundle.putString("json",obj.toString());
                                    bundle.putInt("position",view.getId()-start);
                                    Intent intent=new Intent(context,ShowImage.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            });
                        } catch (Exception e) {
                        }
                    if (thumbUrl.size() == 0)
                        ;
                    else if (thumbUrl.size() <= 3)
                        for (int i = 0; i < 3; i++)
                            imageList[i].setVisibility(View.VISIBLE);
                    else if (thumbUrl.size() <= 6)
                        for (int i = 3; i < 6; i++)
                            imageList[i].setVisibility(View.VISIBLE);
                    else
                        for (int i = 6; i < 9; i++)
                            imageList[i].setVisibility(View.VISIBLE);
                }
                return new MyViewHolder(v1);
            }
            else if(viewType==4){
                if(!obj.getJSONObject("retweeted_status").isEmpty()) {
                    final JSONObject obj1=obj.getJSONObject("retweeted_status");
                    View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.inforretweeted, parent, false);
                    MyRoundImage head = (MyRoundImage) v1.findViewById(R.id.retweeted_head);
                    TextView content=(TextView)v1.findViewById(R.id.retweeted_content),name=(TextView)v1.findViewById(R.id.retweeted_name);
                    content.setText(Html.fromHtml(textTranscation(obj1.getString("text"))));
                    name.setText(obj1.getJSONObject("user").getString("name"));
                    try {
                        Future<Bitmap> future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(obj1.getJSONObject("user").getString("profile_image_url")));
                        head.drawpicture(future.get());
                    }catch (Exception e){}
                    ImageView[] imageList=new ImageView[9];
                    JSONArray array=obj1.getJSONArray("pic_urls");
                    for(int i=0;i<9;i++) {
                        imageList[i] = (ImageView) v1.findViewById(R.id.retweeted_image1 + i);
                        imageList[i].setVisibility(View.GONE);
                    }
                    final int start=imageList[0].getId();
                    for(int i=0;i<array.size();i++){
                        try {
                            imageList[i].setVisibility(View.VISIBLE);
                            Future<Bitmap> future = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(array.getJSONObject(i).getString("thumbnail_pic")));
                            imageList[i].setImageBitmap(future.get());
                            imageList[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle=new Bundle();
                                    bundle.putString("json",obj1.toString());
                                    bundle.putInt("position",view.getId()-start);
                                    Intent intent=new Intent(context,ShowImage.class);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }
                            });
                        }catch (Exception e){}
                    }
                    return new MyViewHolder(v1);
                }
                else {
                    View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.inforretweeted, parent, false);
                    v1.setVisibility(View.GONE);
                    return new MyViewHolder(v1);
                }
            }
            else
                return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return 1;
            else if (position == 1)
                return 2;
            else if (position == 2)
                return 3;
            else if (position == 3)
                return 4;
            else
                return 5;
        }

        private String textTranscation(String text){
            pattern1=Pattern.compile("\\[[a-zA-Z\\u4e00-\\u9fa5]*\\]");
            pattern3=Pattern.compile("@[a-zA-Z0-9-\\u4e00-\\u9fa5]*");
            pattern4=Pattern.compile("#[a-zA-Z0-9\\u4e00-\\u9fa5]*#");

            Matcher m=pattern1.matcher(text);
            while(m.find())
                text = text.replaceAll(m.group(), "");
            text=text.replaceAll("\\[","");
            text=text.replaceAll("\\]","");
            m=pattern3.matcher(text);
            while(m.find())
                text=text.replaceAll(m.group(),"<font color='#FF1943'>"+m.group()+"</font>");
            m=pattern4.matcher(text);
            while(m.find())
                text = text.replaceAll(m.group(), "<font color='#00FFFF'>" + m.group() + "</font>");
            return text;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View v1) {
            super(v1);
        }
    }
}
