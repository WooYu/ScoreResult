package com.rain.scoreresult.scoreresult.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.rain.scoreresult.scoreresult.R;

/**
 * Created by wy201 on 2017-09-23.
 * 带边框的文本
 */

public class TextView_Border extends android.support.v7.widget.AppCompatTextView {


    private int mBorderColor;
    private Paint mPaintOfBorder;
    private Paint mTextPaint;
    private float mBorderWidth;

    public TextView_Border(Context context) {
        this(context,null);
    }

    public TextView_Border(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextView_Border(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextWithBorders);
        mBorderColor = typedArray.getColor(R.styleable.TextWithBorders_borderColor, Color.WHITE);
        mBorderWidth = typedArray.getDimension(R.styleable.TextWithBorders_borderWidth,5);
        typedArray.recycle();

        initBorderPaint();
    }

    private void initBorderPaint(){
        mTextPaint = getPaint();
        mPaintOfBorder = new Paint();
        mPaintOfBorder.setTextSize(mTextPaint.getTextSize());
        mPaintOfBorder.setTypeface(mTextPaint.getTypeface());
        mPaintOfBorder.setFlags(mTextPaint.getFlags());
        mPaintOfBorder.setAlpha(mTextPaint.getAlpha());
        mPaintOfBorder.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintOfBorder.setColor(mBorderColor);
        mPaintOfBorder.setStrokeWidth(mBorderWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        String content = getText().toString();
        canvas.drawText(content,0,getBaseline(),mPaintOfBorder);

        mTextPaint.setStrokeWidth(0);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(content,0,getBaseline(),mTextPaint);
    }
}
