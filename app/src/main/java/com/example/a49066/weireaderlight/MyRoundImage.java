package com.example.a49066.weireaderlight;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class MyRoundImage extends android.support.v7.widget.AppCompatImageView {

    BitmapShader shader;
    Paint p;
    Bitmap bmp;
    String userHeadString;
    int bmpflag=0;

    public MyRoundImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRoundImage(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (bmp != null) {
            float scalf = (float) canvas.getWidth() / bmp.getWidth();
            int x = canvas.getWidth() / 2;

            Matrix matrix = new Matrix();
            matrix.setScale(scalf, scalf);

            shader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            shader.setLocalMatrix(matrix);

            p = new Paint();
            p.setShader(shader);
            canvas.drawCircle(x, x, x, p);
        }
    }

    public void drawpicture(Bitmap originbmp){
        this.bmp=originbmp;
        postInvalidate();
    }
}
