package com.rain.scoreresult.scoreresult.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rain.scoreresult.scoreresult.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wy201 on 2017-09-23.
 * 音调的打分
 */

public class ScoreView_Tone extends View {
    //颜色
    //边框颜色
    private final int COLOR_BORDER = Color.argb(187, 0, 255, 255);
    //黑色
    private final int COLOR_BLACK = Color.argb(187, 0, 0, 0);
    //黄色
    private final int COLOR_YELLOW = Color.argb(187, 255, 255, 129);
    //粉色
    private final int COLOR_RED = Color.argb(187, 255, 128, 140);
    //绿色
    private final int COLOR_INDIGO = Color.argb(187, 140, 255, 255);

    //度量
    //长方形宽度
    private int mWidthOfRectangle;
    //长方形高度
    private int mHeightOfRectangle;
    //长方形数量
    private final int NUMBEROFRECTANGLE = 24;
    //长方形间隙
    private int mSpaceOfRectangle;
    //圆角
    private float mSizeOfRoundCircle;
    //边框宽度
    private float mSizeOfBorder;

    //画笔
    //背景画笔
    private Paint mPaintOfBackground;
    //前景画笔
    private Paint mPaintOfForeground;

    //数据
    private List<Integer> mScoreList;
    private List<Integer> mTempList;
    private int[] colorArr = new int[]{COLOR_BLACK, COLOR_YELLOW, COLOR_RED, COLOR_INDIGO};


    public ScoreView_Tone(Context context) {
        this(context, null);
    }

    public ScoreView_Tone(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView_Tone(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initSize();
        initPaint();
        fillingData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidthOfRectangle = getMeasuredWidth() / NUMBEROFRECTANGLE - mSpaceOfRectangle;
        mHeightOfRectangle = getMeasuredHeight();

        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {

            float left = i * (mWidthOfRectangle + mSpaceOfRectangle);
            float top = 0;
            float right = left + mWidthOfRectangle;
            float bottom = top + mHeightOfRectangle;
            RectF bgRectf = new RectF(left, top, right, bottom);
            RectF fgRectf = new RectF(left + mSizeOfBorder, top + mSizeOfBorder, right - mSizeOfBorder, bottom - mSizeOfBorder);

            //绘制背景
            canvas.drawRoundRect(fgRectf, mSizeOfRoundCircle, mSizeOfRoundCircle, mPaintOfBackground);

            //绘制前景
            mPaintOfForeground.setColor(colorArr[mTempList.get(i)]);
            canvas.drawRoundRect(fgRectf, mSizeOfRoundCircle, mSizeOfRoundCircle, mPaintOfForeground);
        }
    }

    private void initPaint() {
        mPaintOfBackground = new Paint();
        mPaintOfBackground.setAntiAlias(true);
        mPaintOfBackground.setStrokeWidth(mSizeOfBorder);
        mPaintOfBackground.setStyle(Paint.Style.STROKE);
        mPaintOfBackground.setColor(COLOR_BORDER);

        mPaintOfForeground = new Paint();
        mPaintOfForeground.setStyle(Paint.Style.FILL);
        mPaintOfForeground.setAntiAlias(true);

    }

    private void initSize() {
        mSizeOfBorder = ConvertUtils.dp2px(getContext(), 2);
        mSpaceOfRectangle = ConvertUtils.dp2px(getContext(), 1);
        mSizeOfRoundCircle = ConvertUtils.dp2px(getContext(), 3);
    }

    private void fillingData() {
        mScoreList = new ArrayList<>();
        mTempList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {
            mScoreList.add(random.nextInt(4));
            mTempList.add(0);
        }

        ValueAnimator valueanimator = ValueAnimator.ofInt(0, NUMBEROFRECTANGLE - 1);
        valueanimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mTempList.set(value, mScoreList.get(value));
                invalidate();
            }
        });
        valueanimator.setDuration(3000);
        valueanimator.start();
    }
}
