package com.example.a49066.weireaderlight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends android.support.v7.widget.AppCompatImageView {
    public MyImageView(Context context){
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public MyImageView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }

    @Override
    protected void onMeasure(int width,int height){
        super.onMeasure(width,width);
    }
}
