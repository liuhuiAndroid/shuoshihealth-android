package com.ssh.shuoshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class VideoRelativeLayout extends RelativeLayout {

    public VideoRelativeLayout(Context context) {
        super(context);
    }

    public VideoRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
