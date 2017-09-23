package com.rain.scoreresult.scoreresult.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.rain.scoreresult.scoreresult.ConvertUtils;

/**
 * Created by wy201 on 2017-09-19.
 * 综合得分
 */

public class TextView_Synthetical extends android.support.v7.widget.AppCompatTextView {

    public TextView_Synthetical(Context context) {
        super(context);
    }

    public TextView_Synthetical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView_Synthetical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint mPaint = getPaint();
        String content = getText().toString();
        Paint.FontMetrics fontMetrics=getPaint().getFontMetrics();
        float baseline = getMeasuredHeight() - fontMetrics.descent;

        //绘制描边
        TextPaint mSyntheticalPaint = new TextPaint();
        mSyntheticalPaint.setTextSize(mPaint.getTextSize());
        mSyntheticalPaint.setTypeface(mPaint.getTypeface());
        mSyntheticalPaint.setFlags(mPaint.getFlags());
        mSyntheticalPaint.setAlpha(mPaint.getAlpha());
        mSyntheticalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSyntheticalPaint.setColor(Color.parseColor("#7d0000"));
        mSyntheticalPaint.setStrokeWidth(ConvertUtils.dp2px(getContext(),5));
        canvas.drawText(content, 0, baseline, mSyntheticalPaint);

        //绘制渐变
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, getMeasuredHeight(),
                new int[]{Color.parseColor("#fcf4d5"), Color.parseColor("#beaa59")}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(content, 0,baseline , mPaint);


    }

}
