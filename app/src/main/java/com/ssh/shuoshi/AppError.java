package com.ssh.shuoshi;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.util.AppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by hwt on 2020/12/8
 */
public class AppError implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "AppError";
    protected boolean isSendEmail = false;
    private Context mContext;

    //系统默认的UncaughtException处理类
    protected Thread.UncaughtExceptionHandler mDefaultHandler;

    /*初始化*/
    public void initUncaught(Context context) {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该Crasshandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context;
    }

    /*当UncaughtException发生时会转入该函数来处理*/
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //AppException.exc(e).makeToast(AppContext.getContext());
            }
            //退出程序
            AppManager.getAppManager().appExit(MyApplication.getContext());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        ToastUtil.showToast("很抱歉,程序出现异常,即将退出.");
        if (isSendEmail) {
            sendErrorInfoMail(ex);
        }
        //保存日志文件
        saveErrorLog(ex);
        return true;
    }

    public void sendErrorInfoMail(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        sb.append("--------------------" + (new Date().toLocaleString()) + "---------------------\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

    }

    /**
     * 保存异常日志
     */
    public void saveErrorLog(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        //在控制台打印
        System.err.println(sb.toString());

        String errorlog = "throwable_log" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt";
        String savePath = "";
        //        String logFilePath = "";
        File logDir = null;
        FileWriter fw = null;
        PrintWriter pw = null;
        try {
            //判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                //Environment.getExternalStorageDirectory().getAbsolutePath()
                savePath = MyApplication.getContext().getExternalCacheDir() + Constants.LH_LOG_PATH;
                logDir = new File(savePath);
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }
            }
            //没有挂载SD卡，无法写文件
            if (logDir == null || !logDir.exists() || !logDir.canWrite()) {
                return;
            }
            File logFile = new File(logDir, errorlog);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            fw = new FileWriter(logFile, true);
            pw = new PrintWriter(fw);
            pw.println("--------------------" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())) + "---------------------");
            writeMobileInfo(mContext, pw);
            pw.write(sb.toString());
            pw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void writeMobileInfo(Context context, PrintWriter pw) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            pw.print("App Version:");
            pw.print(pi.versionName);
            pw.print("_");
            pw.println(pi.versionCode);
            pw.print("OS Version:");
            pw.print(Build.VERSION.RELEASE);
            pw.print("_");
            pw.println(Build.VERSION.SDK_INT);
            pw.print("Vendor:");
            pw.println(Build.MANUFACTURER);
            pw.print("Model:");
            pw.println(Build.MODEL);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeString(String file, String content, String charset, boolean isNext) {
        try {
            byte[] data = content.getBytes(charset);
            writeBytes(file, data, isNext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean writeBytes(String filePath, byte[] data, boolean isNext) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath, isNext);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
