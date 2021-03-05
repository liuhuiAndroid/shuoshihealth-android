package com.ssh.shuoshi.library.util;

import android.view.View;
import android.view.animation.AlphaAnimation;

/**
 * Created by WE-WIN-027 on 2016/7/8.
 *
 * @des ${TODO}
 */
public class AnimationUtils {
    public static void showAlpha(View v) {
        v.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(200);
        v.startAnimation(alphaAnimation);
    }

    public static void hideAlpha(View v) {
        v.setVisibility(View.GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        v.startAnimation(alphaAnimation);
    }
}
