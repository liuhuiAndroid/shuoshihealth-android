package com.ssh.shuoshi.library.util.imageloader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.ssh.shuoshi.library.util.CommonUtils;
import com.ssh.shuoshi.library.util.imageloader.listener.ImageSaveListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by soulrelay on 2016/10/11 13:48.
 * Class Note:
 * using {@link Glide} to load image
 * 默认采用Glide策略
 */
public class GlideImageLoaderStrategy implements BaseImageLoaderStrategy {

    @Override
    public void loadImage(String url, int placeholder, ImageView imageView) {
//        loadNormal(imageView.getContext(), url, placeholder, imageView);
    }

    @Override
    public void loadImage(Context context, String url, int placeholder, ImageView imageView) {
//        loadNormal(context, url, placeholder, imageView);
    }


    @Override
    public void loadImage(Context context, Integer resourceId, int placeholder, ImageView imageView) {
        loadDrawable(context, resourceId, placeholder, imageView);
    }

    /**
     * 无holder的gif加载
     *
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void loadCircleImage(String url, int placeholder, ImageView imageView) {
//        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
//                .transform(new GlideCircleTransform(imageView.getContext()))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor) {
//        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
//                .transform(new GlideCircleTransform(imageView.getContext(),borderWidth,borderColor))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void loadCircleBorderImage(String url, int placeholder, ImageView imageView, float borderWidth, int borderColor, int heightPx, int widthPx) {
//        Glide.with(imageView.getContext()).load(url).placeholder(placeholder).dontAnimate()
//                .transform(new GlideCircleTransform(imageView.getContext(),borderWidth,borderColor,heightPx,widthPx))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    @Override
    public void loadImageWithAppCxt(String url, ImageView imageView) {
        // 使用ApplicationContext防止You cannot start a load for a destroyed activity.
        Glide.with(imageView.getContext().getApplicationContext()).load(url)
                .placeholder(imageView.getDrawable())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context.getApplicationContext()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.getApplicationContext()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void trimMemory(Context context, int level) {
        Glide.get(context).trimMemory(level);
    }

    @Override
    public String getCacheSize(Context context) {
        try {
            return CommonUtils.getFormatSize(CommonUtils.getFolderSize(Glide.getPhotoCacheDir(context.getApplicationContext())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void saveImage(Context context, String url, String savePath, String saveFileName, ImageSaveListener listener) {
        if (!CommonUtils.isSDCardExsit() || TextUtils.isEmpty(url)) {
            listener.onSaveFail();
            return;
        }
        InputStream fromStream = null;
        OutputStream toStream = null;
        try {
            File cacheFile = Glide.with(context).load(url).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
            if (cacheFile == null || !cacheFile.exists()) {
                listener.onSaveFail();
                return;
            }
            File dir = new File(savePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, saveFileName + CommonUtils.getPicType(cacheFile.getAbsolutePath()));

            fromStream = new FileInputStream(cacheFile);
            toStream = new FileOutputStream(file);
            byte length[] = new byte[1024];
            int count;
            while ((count = fromStream.read(length)) > 0) {
                toStream.write(length, 0, count);
            }
            //用广播通知相册进行更新相册
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            listener.onSaveSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            listener.onSaveFail();
        } finally {
            if (fromStream != null) {
                try {
                    fromStream.close();
                    toStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fromStream = null;
                    toStream = null;
                }
            }
        }
    }

    private void loadDrawable(final Context ctx, final Integer resourceId, int placeholder, ImageView imageView) {
        Glide.with(ctx).load(resourceId).dontAnimate()
                .placeholder(placeholder)
                .into(imageView);
    }
}
