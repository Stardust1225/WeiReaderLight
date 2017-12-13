package com.example.a49066.weireaderlight;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


public class ShowImage extends Activity {
    ViewPager pager;
    View[] viewList;
    PagerAdapter pageradapter;
    JSONArray array;
    @Override
    public void onCreate(Bundle bundle1){
        super.onCreate(bundle1);
        Bundle bundle=this.getIntent().getExtras();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.originimageshow);
        JSONObject obj=JSONObject.fromObject(bundle.getString("json"));
        int position=bundle.getInt("position");
        System.out.println(position);
        pager=(ViewPager)findViewById(R.id.origin_viewpager);
        array=obj.getJSONArray("pic_urls");
        Future<Bitmap>[] future=new Future[array.size()];
        viewList=new View[array.size()];
        pageradapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return array.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object object){
                container.removeView(viewList[position]);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList[position]);
                return viewList[position];
            }

            @Override
            public int getItemPosition(Object object){
                return POSITION_NONE;
            }
        };
        pager.setAdapter(pageradapter);

        for(int i=0;i<array.size();i++) {
            future[i] = BitmapLoader.BitmapThreadPool.submit(new BitmapLoader(array.getJSONObject(i).getString("thumbnail_pic").replaceAll("thumbnail", "bmiddle")));
            viewList[i]=null;
        }

        try {
            View v = getLayoutInflater().inflate(R.layout.originimage, null);
            ImageView imageview = v.findViewById(R.id.origin_imageview);
            imageview.setImageBitmap(future[position].get());
            viewList[position]=v;
            pageradapter.notifyDataSetChanged();
            pager.setCurrentItem(position,true);
        }catch (Exception e){}

        for(int i=0;i<array.size();i++){
            try {
                View v = getLayoutInflater().inflate(R.layout.originimage, null);
                ImageView imageview = v.findViewById(R.id.origin_imageview);
                imageview.setImageBitmap(future[i].get());
                viewList[i]=v;
            }catch (Exception e){}
        }
        pageradapter.notifyDataSetChanged();

    }

}
