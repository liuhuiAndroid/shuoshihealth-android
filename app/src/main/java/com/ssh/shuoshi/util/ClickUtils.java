package com.ssh.shuoshi.util;

/**
 * 快速点击控制重复页面
 * created by hwt on 2021/1/10
 */
public class ClickUtils {
    // 两次点击按钮之间的点击间隔不能少于800毫秒
    private static final int MIN_CLICK_DELAY_TIME = 800;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
