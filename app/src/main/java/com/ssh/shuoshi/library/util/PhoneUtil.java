package com.ssh.shuoshi.library.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssh.shuoshi.Constants.PARAMETERS_CHECK_IDENTITY_NO;
import static com.ssh.shuoshi.Constants.PARAMETERS_CHECK_PASSWORD;

/**
 * Created by WE-WIN-027 on 2016/7/5.
 *
 * @des ${TODO}
 */
public class PhoneUtil {

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        try {
            String id;
            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            if (mTelephony.getDeviceId() != null) {
                id = mTelephony.getDeviceId();
            } else {
                id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            if (null == id) {
                return "";
            }
            return id;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }


    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^((1[83][0-9])|(14[57])|(17[035678])|(15[012356789])|(166)|(19[89]))\\d{8}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 密码验证
     */
    public static boolean isPassword(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(PARAMETERS_CHECK_PASSWORD);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 身份证验证
     */
    public static boolean isIdCard(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile(PARAMETERS_CHECK_IDENTITY_NO);
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}