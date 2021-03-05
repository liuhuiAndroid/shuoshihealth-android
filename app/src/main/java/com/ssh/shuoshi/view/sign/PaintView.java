package com.ssh.shuoshi.view.sign;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * author ：hwt
 * date : 2019/11/19 16:12
 */
public class PaintView extends View {
    private Paint paint;
    private Canvas cacheCanvas;
    private Bitmap cachebBitmap;
    private Path path;
    private Context mContext;

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    private boolean isEmpty = true;

    //判断路径是否为空
    public boolean pathIsEmpty() {
        return isEmpty;
    }

    public Bitmap getCachebBitmap() {
        return cachebBitmap;
    }

    public PaintView(Context context) {
        super(context);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置笔画宽度
        paint.setStrokeWidth(12);
        //设置签名笔画样式
        paint.setStyle(Paint.Style.STROKE);
        //设置签名颜色
        paint.setColor(Color.BLACK);
        path = new Path();
    }

    public void clear() {
        if (cacheCanvas != null) {
            isEmpty = true;
//            paint.setColor(Color.WHITE);
//            cacheCanvas.drawPaint(paint);
            cacheCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            paint.setColor(Color.BLACK);
            invalidate();
        }
    }

    private void ensureSignatureBitmap() {
        if (cachebBitmap == null) {
            cachebBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

            cacheCanvas = new Canvas(cachebBitmap);

            //设置背景图图片  要指定图片大小  否则下面注释的方法会放大
            cacheCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sign_bg),
                    new Rect(0, 0, BitmapFactory.decodeResource(getResources(), R.drawable.sign_bg).getWidth(),
                            BitmapFactory.decodeResource(getResources(), R.drawable.sign_bg).getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()), paint);
//            mSignatureBitmapCanvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg_splash), matrix, mPaint);

        }
    }

    public String save(String path) throws IOException {
        Bitmap bitmap = cachebBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File cacheDir = mContext.getExternalCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            File file = new File(cacheDir, path);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
            return file.getAbsolutePath();
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(cachebBitmap, 0, 0, paint);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //创建跟view一样大的bitmap，用来保存签名
        cachebBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(Color.TRANSPARENT);
    }

    private float cur_x, cur_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                isEmpty = false;
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }

            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }

        invalidate();

        return true;
    }
}
