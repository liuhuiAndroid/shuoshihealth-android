package com.ssh.shuoshi.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.RangeMonthView;

import static com.ssh.shuoshi.library.util.ScreenUtil.dipToPx;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class SimpleMonthView extends RangeMonthView {

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    private int mRadius;

    public SimpleMonthView(Context context) {
        super(context);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.parseColor("#FFFF824D"));

    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    //绘制背景
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelectedPre, boolean isSelectedNext, boolean isToday) {

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);
        mSelectedPaint.setColor(Color.parseColor("#EEEEEE"));

        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        if (isSelectedPre) {
            if (isSelectedNext) {
                canvas.drawRect(x - 8, cy - mRadius - 8, x + mItemWidth + 10, cy + mRadius + 12, mSelectedPaint);
            } else {//最后一个，the last
                canvas.drawRect(x - 8, cy - mRadius - 8, cx + 10, cy + mRadius + 12, mSelectedPaint);
            }
        } else {
            if (isSelectedNext) {
                canvas.drawRect(cx - 8, cy - mRadius - 8, x + mItemWidth + 10, cy + mRadius + 12, mSelectedPaint);
            }
        }

        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setStrokeWidth(2);
        if (isToday) {
            mSelectedPaint.setColor(Color.parseColor("#FFFF824D"));
        } else {
            mSelectedPaint.setColor(Color.parseColor("#FFEEEEEE"));
        }

        canvas.drawCircle(cx, cy + 2, mRadius + 10, mSelectedPaint);
        return false;
    }

    //绘制下划线
    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y, boolean isSelected) {
        RectF rectF = new RectF();
        rectF.left = x + mItemWidth / 2 - 20;
        rectF.right = x + mItemWidth / 2 + 20;
        rectF.top = y + mItemHeight - 3 * mPadding + 10;
        rectF.bottom = y + mItemHeight - 3 * mPadding + 20;
        canvas.drawRoundRect(rectF, 5, 5, mPointPaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected, boolean isToday) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isToday) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
