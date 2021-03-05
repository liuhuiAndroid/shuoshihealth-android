package com.ssh.shuoshi.view.title;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssh.shuoshi.R;


public class UniteTitle extends RelativeLayout {

    private RelativeLayout layoutRoot;
    private TextView titleTextView;
    private RelativeLayout leftLayout;
    LinearLayout rightLayout;
    private ImageView leftImage;
    private TextView leftTextView;
    private ImageView rightImage1;
    private ImageView rightImage2;
    private TextView rightTextView;
//    private OnClickListener backOnClickListener;

    public UniteTitle(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_my_title, this);
        init();
    }

    public UniteTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_my_title, this);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.UniteTitle);
        if (typedArray != null) {
            boolean titleVisible = typedArray.getBoolean(R.styleable.UniteTitle_titleVisible, true);
            if (titleVisible) {
                String titleStr = typedArray.getString(R.styleable.UniteTitle_titleText);
                if (!TextUtils.isEmpty(titleStr)) {
                    titleTextView.setText(titleStr);
                }
            } else {
                titleTextView.setVisibility(View.INVISIBLE);
            }


            boolean leftVisible = typedArray.getBoolean(R.styleable.UniteTitle_leftVisible, true);
            if (leftVisible) {
                String leftStr = typedArray.getString(R.styleable.UniteTitle_leftText);
                if (!TextUtils.isEmpty(leftStr)) {
                    leftTextView.setVisibility(View.VISIBLE);
                    leftTextView.setText(leftStr);
                }
            } else {
                leftLayout.setVisibility(View.INVISIBLE);
            }
            boolean rightVisible = typedArray.getBoolean(R.styleable.UniteTitle_rightVisible, true);
            if (rightVisible) {
                String rightStr = typedArray.getString(R.styleable.UniteTitle_rightText);
                if (!TextUtils.isEmpty(rightStr)) {
                    rightTextView.setVisibility(View.VISIBLE);
                    rightTextView.setText(rightStr);
                }


            } else {
                rightLayout.setVisibility(View.INVISIBLE);
            }
            typedArray.recycle();
        }
    }

    private void init() {
        layoutRoot = (RelativeLayout) findViewById(R.id.layout_title_root);
        titleTextView = (TextView) findViewById(R.id.title_text);
        leftLayout = (RelativeLayout) findViewById(R.id.left_layout);
        leftImage = (ImageView) findViewById(R.id.left_image);
        leftTextView = (TextView) findViewById(R.id.left_text);
        rightImage1 = (ImageView) findViewById(R.id.right_image1);
        rightImage2 = (ImageView) findViewById(R.id.right_image2);
        rightTextView = (TextView) findViewById(R.id.right_text);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        layoutRoot.setBackgroundColor(color);
    }

    /**
     * 设置左边返回键
     *
     * @param backDrawableId
     * @param onClickListener
     */
    public void setBackListener(int backDrawableId, OnClickListener onClickListener) {
        if (backDrawableId != -1) {
            leftImage.setImageResource(backDrawableId);
        }
        setBackOnClickListener(onClickListener);
    }


    /**
     * 设置左边返回键
     *
     * @param backDrawableId
     * @param text
     * @param onClickListener
     */
    public void setBackListener(int backDrawableId, String text, OnClickListener onClickListener) {
        if (backDrawableId != -1) {
            leftImage.setImageResource(backDrawableId);
        }
        setBackListener(text, onClickListener);
    }

    /**
     * 设置左边返回键
     *
     * @param text
     * @param onClickListener
     */
    public void setBackListener(String text, OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) {
            leftTextView.setVisibility(VISIBLE);
            leftTextView.setText(text);
        }
        setNewBackOnClickListener(onClickListener);
    }

    public void setNewBackOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            leftLayout.setOnClickListener(onClickListener);
            leftImage.setVisibility(View.GONE);
        }
    }

    public void setBackOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            leftLayout.setOnClickListener(onClickListener);
            leftImage.setVisibility(View.VISIBLE);
        } else {
            leftImage.setVisibility(View.GONE);
        }
    }

    /**
     * 标题文字
     *
     * @param name
     */
    public void setTitleName(String name) {
        if (name != null) {
            titleTextView.setText(name);
        }
    }

    public void setRightButton(int backDrawableId) {
        try {
            if (backDrawableId != -1) {
                rightImage1.setImageResource(backDrawableId);
            }
        } catch (Resources.NotFoundException e) {

        }
    }

    public void setRightButton(int backDrawableId, OnClickListener onClickListener) {

        if (backDrawableId != -1) {
            rightImage1.setImageResource(backDrawableId);
        }
        if (onClickListener != null) {
            rightImage1.setVisibility(View.VISIBLE);
            rightImage1.setOnClickListener(onClickListener);
        } else {
            rightImage1.setVisibility(View.GONE);
        }
    }

    public void setRightButton2(int rightDrawableId, OnClickListener onClickListener) {
        if (rightDrawableId != -1) {
            rightImage2.setVisibility(View.VISIBLE);
            rightImage2.setImageResource(rightDrawableId);
        }
        if (onClickListener != null) {
            rightImage2.setOnClickListener(onClickListener);
        } else {
            rightImage2.setVisibility(View.GONE);
        }
    }

    public void hideRight() {
        rightLayout.setVisibility(View.INVISIBLE);
    }

    public void showRight() {
        rightLayout.setVisibility(View.VISIBLE);
    }

    public void setRightStr(int resId) {
        try {
            rightTextView.setText(resId);
        } catch (Resources.NotFoundException e) {

        }
    }

    /**
     * 设置右侧按钮
     *
     * @param str
     * @param onClickListener
     */
    public void setRightButton(String str, OnClickListener onClickListener) {
        rightTextView.setText(str);
        rightTextView.setVisibility(View.VISIBLE);
        setRightListener(onClickListener);
    }

    public void setRightButtonColor(int color) {
        rightTextView.setTextColor(color);
        rightTextView.setVisibility(View.VISIBLE);
    }

    public void setRightListener(OnClickListener listener) {
        if (listener != null) {
            rightLayout.setOnClickListener(listener);
        }
    }

    /**
     * 返回对象，用于同步
     *
     * @return
     */
    public ImageView getSyncView() {
        return rightImage1;
    }


    public void setLineVisible(boolean visible) {
        findViewById(R.id.title_line).setVisibility(visible ? VISIBLE : GONE);
    }
}
