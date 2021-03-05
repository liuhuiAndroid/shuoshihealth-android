package com.ssh.shuoshi.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.ssh.shuoshi.MyApplication;
import com.ssh.shuoshi.library.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * created by hwt on 2020/12/8
 */
public class PhotoUtils {

    private static String DEFAULT_DISK_CACHE_DIR = "ss_disk_cache";


    public static File getCacheDir() {
        File cacheDir = MyApplication.getContext().getExternalCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir;
    }

    private static File getPhotoCacheDir(Context context) {
        return getPhotoCacheDir(context, DEFAULT_DISK_CACHE_DIR);
    }

    private static File getPhotoCacheDir(Context context, String cacheName) {
        File cacheDir = context.getCacheDir();
        if (cacheDir == null) {

            return null;
        } else {
            File result = new File(cacheDir, cacheName);
            return result.mkdirs() || result.exists() && result.isDirectory() ? result : null;
        }
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public static String takePicture(Activity activity, int requestCode, long time, File dir) {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    requestCode);
            return null;
        } else {
            Intent intentP = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentP.addCategory(Intent.CATEGORY_DEFAULT);
            File file = new File(dir, time + ".jpg");
            Uri contentUri = null;
            if (Build.VERSION.SDK_INT >= 23) {
                contentUri = FileProvider.getUriForFile(activity, "com.ssh.shuoshi.fileprovider", file);
                intentP.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                contentUri = Uri.fromFile(file);
            }
            intentP.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            activity.startActivityForResult(intentP, requestCode);
            return file.getAbsolutePath();
        }
    }

    public static String takePhotos(Activity activity, int requestCode, long time) {

        return takePicture(activity, requestCode, time, getCacheDir());
    }

    public static void saveBitmap(String path, Bitmap bm) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap decodeSampleBitmapFromFile(String path,
                                                    int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / reqWidth;
        int beHeight = h / reqHeight;
        int scale;
        if (beWidth < beHeight) {
            scale = beWidth;
        } else {
            scale = beHeight;
        }
        if (scale <= 0) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false; // 设为 false
//        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap decodeSampleBitmapFromFile(byte[] buff) {
        return BitmapFactory.decodeByteArray(buff, 0, buff.length);
    }

    public static Bitmap decodeSampleBitmapFromFile(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false; // 设为 false
//        options.inSampleSize = calculateSampleSize(options,reqWidth,reqHeight);
        return BitmapFactory.decodeFile(path, options);
    }


    public static void deletePic(List<String> list) {
//        File[] files = PhotoUtils.getCacheDir().listFiles();
//        for (File file : files) {
//            if (file.getName().equals(photoName)) {
//                file.delete();
//                break;
//            }
//        }
        if (list == null || list.size() == 0) return;
        File[] files = PhotoUtils.getCacheDir().listFiles();
        for (int i = 0; i < list.size(); i++) {
            for (File file : files) {
                if (list.get(i).equals(file.getAbsolutePath())) {
                    file.delete();
                }
            }
        }
    }

    //采样压缩+质量压缩
    public static void compress(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //获取原图的宽和高
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        //压缩比率计算
        int wRation = (int) Math.ceil(outWidth / (float) 3000);
        int hRation = (int) Math.ceil(outHeight / (float) 3000);
//        Log.i("hwt", "宽比率:  " + wRation);
//        Log.i("hwt", "高比率:  " + hRation);
        if (wRation > 1 && hRation > 1) {
            if (wRation > hRation) {
                options.inSampleSize = wRation;
            } else {
                options.inSampleSize = hRation;
            }
        }
        options.inJustDecodeBounds = false;
        Bitmap resultBitmap = BitmapFactory.decodeFile(filePath, options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int ops = 100;
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, ops, bos);//quality:100=215.38kb,80=50.41kb
        while (bos.toByteArray().length / 1024 > 2048) {
            ops -= 10;
            if (ops <= 0) {
                break;
            }
            bos.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, ops, bos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 图片质量压缩
    public static byte[] compressBmpToFile(String picturePath) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmp = getBitmap(picturePath);
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 2048) {
            options -= 10;
            if (options <= 0) {
                break;
            }
            baos.reset();
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        //写出
        File pic = new File(picturePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pic);
            fileOutputStream.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return baos.toByteArray();
    }


    public static Bitmap getBitmap(String imgPath) {
        // Get bitmap through image path
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = false;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // Do not compress
        newOpts.inSampleSize = 1;
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
        Log.e("12312312312313", "bitmap == " + bitmap.getWidth() + "      imgPath == " + imgPath);
        return BitmapFactory.decodeFile(imgPath, newOpts);
    }

    public static Bitmap rotateImageView(String path, int angle) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if (bitmap == null) {
            return null;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }


    //保存Bitmap图片
    public static String saveNewBitmap(String name, Bitmap bm) {
        String targetPath = getCacheDir() + "/images/";
        if (!fileExist(targetPath)) {
            ToastUtil.showToast("创建文件夹失败");
            Log.i("hwt", "创建文件夹失败");
            return "";
        } else {
            //文件夹创建完成，开始保存
            File saveFile = new File(targetPath, name);
            try {
                FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, saveImgOut);
                saveImgOut.flush();
                saveImgOut.close();
                return saveFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
    }


    public static boolean fileExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return file.mkdirs();
        }
    }

    //通过新图片覆盖路径
    public static void saveMyBitmap(String path, Bitmap bitmap) {
        //写出
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        while (bos.toByteArray().length / 1024 > 2048) {
            options -= 10;
            if (options <= 0) {
                break;
            }
            bos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, bos);
        }

        File pic = new File(path);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(pic);
            fileOutputStream.write(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("hwt", "结束时间: " + System.currentTimeMillis());
    }

    public static boolean isBase64Img(String imgUrl) {
        if (!TextUtils.isEmpty(imgUrl) && (imgUrl.startsWith("data:image/png;base64,")
                || imgUrl.startsWith("data:image/*;base64,") || imgUrl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }
}
