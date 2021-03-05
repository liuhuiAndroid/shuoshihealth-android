package com.ssh.shuoshi.library.util;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;

import com.ssh.shuoshi.R;


/**
 * Created by sll on 2015/9/6 0006.
 */
public class ResourceUtil {

    public static int getThemeColor(@NonNull Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }

    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
