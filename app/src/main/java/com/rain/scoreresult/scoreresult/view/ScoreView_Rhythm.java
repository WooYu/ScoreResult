package com.rain.scoreresult.scoreresult.view;

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
 * 节奏的打分
 */

public class ScoreView_Rhythm extends View {
    //颜色
    //边框颜色
    private final int COLOR_BORDER = Color.argb(255, 0, 255, 255);
    //黑色
    private final int COLOR_BLACK = Color.argb(255, 0, 0, 0);
    //绿色
    private final int COLOR_INDIGO = Color.argb(187, 140, 255, 255);

    //度量
    //长方形宽度
    private int mWidthOfRectangle;
    //长方形高度
    private int mHeightOfRectangle;
    //长方形数量
    private final int NUMBEROFRECTANGLE = 10;
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
    private int[] colorArr = new int[]{COLOR_BLACK, COLOR_INDIGO};

    //数据
    //节奏得分。分两部分，右边对应的数字是1~5。左边部分，左往右数（-1~-5）负1
    private int score;
    private List<Integer> mScoreList;

    public ScoreView_Rhythm(Context context) {
        this(context, null);
    }

    public ScoreView_Rhythm(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView_Rhythm(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        float heightOfCurRect = mHeightOfRectangle * 1 / 2;
        float incrementOfHeight = (mHeightOfRectangle - heightOfCurRect) * 2 / NUMBEROFRECTANGLE;
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {

            float left = i * (mWidthOfRectangle + mSpaceOfRectangle);
            float top = (getMeasuredHeight() - heightOfCurRect) / 2;
            float right = left + mWidthOfRectangle;
            float bottom = top + heightOfCurRect;
            RectF fgRectf = new RectF(left + mSizeOfBorder, top + mSizeOfBorder, right - mSizeOfBorder, bottom - mSizeOfBorder);

            //绘制背景
            canvas.drawRoundRect(fgRectf, mSizeOfRoundCircle, mSizeOfRoundCircle, mPaintOfBackground);

            //绘制前景
            mPaintOfForeground.setColor(colorArr[mScoreList.get(i)]);
            canvas.drawRoundRect(fgRectf, mSizeOfRoundCircle, mSizeOfRoundCircle, mPaintOfForeground);

            //更改变量
            if (i < NUMBEROFRECTANGLE / 2) {
                heightOfCurRect += incrementOfHeight;
            } else if (i > NUMBEROFRECTANGLE / 2) {
                heightOfCurRect -= incrementOfHeight;
            }

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
        mSpaceOfRectangle = ConvertUtils.dp2px(getContext(), 0);
        mSizeOfRoundCircle = ConvertUtils.dp2px(getContext(), 3);
    }

    private void fillingData() {
        Random random = new Random();
        while (score == 0) {
            score = random.nextInt(NUMBEROFRECTANGLE) - NUMBEROFRECTANGLE / 2;
        }

        mScoreList = new ArrayList<>();
        for (int i = 0; i < NUMBEROFRECTANGLE; i++) {
            mScoreList.add(0);
        }

        int startvalue;
        int endvalue;
        if (score < 0) {
            startvalue = NUMBEROFRECTANGLE / 2 + score;
            endvalue = NUMBEROFRECTANGLE / 2;
        } else {
            startvalue = NUMBEROFRECTANGLE / 2;
            endvalue = NUMBEROFRECTANGLE / 2 + score;
        }

        for (int i = startvalue; i < endvalue; i++) {
            mScoreList.set(i, 1);
        }

    }
}

