package com.rain.scoreresult.scoreresult.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rain.scoreresult.scoreresult.ConvertUtils;
import com.rain.scoreresult.scoreresult.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.rain.scoreresult.scoreresult.ConvertUtils.degreeToRadian;

/**
 * Created by wy201 on 2017-09-20.
 * 雷达图
 */

public class RadarChart extends View {
    //数据
    //传递进来的数据：节奏、安定性、长音、表现力、音程
    private ArrayList<String> mScoreDataList;
    //每层五边形顶点的数据
    private HashMap<Integer, List<Point>> mVertexDataOfPerLayer;
    //平均值的顶点数据
    private List<Point> mVertexDataOfAverage;
    //得分的顶点数据
    private List<Point> mVertexDataOfScore;
    //得分的初始数值
    private List<Point> mVertexDataOfInitScore;

    //颜色
    //雷达填充色
    private final int COLOR_REDAR = Color.parseColor("#990080a2");
    //平均值填充色
    private final int COLOR_AVERAGE = Color.parseColor("#EECC8056");
    //得分填充色
    private final int COLOR_SCORE = Color.parseColor("#a19cedf3");
    //文字框填充色
    private final int COLOR_EXPLAIN = Color.parseColor("#000023");
    //边框颜色：白
    private final int COLOR_BORDERWHITE = Color.WHITE;
    //边框颜色：靛
    private final int COLOR_BORDERINDIGO = Color.parseColor("#00ffff");
    //边框颜色:蓝
    private final int COLOR_BORDERBLUE = Color.parseColor("#2c14b7");
    //中心点
    private Point mPointOfCenter;

    //度量
    //屏幕宽
    private float mScreenWidth;
    //文字和雷达图间距
    private float mSpaceOfTextImg;
    //控件最小宽
    private float minViewWidth;
    //控件最小高
    private float minViewHeight;
    //控件宽
    private float mViewWidth;
    //控件高
    private float mViewHeight;
    //字体大小
    private float mSizeOfTypeface;
    //外边线宽度
    private float mOuterEdgeWidth;
    //内边线宽度
    private float mInnerEdgeWidth;
    //正方形点大小(对角线)
    private float mPointSizeOfSquare;
    //白色圆点大小
    private float mPointSizeOfCircle;
    //环行线数量
    private final int mNumOfPlies = 11;
    //最小的环线间距
    private float minSpaceOfLoopLine;
    //最里层环线半径
    private float mRadiusOfTheInnermost;
    //最外层环线半径
    private float mRadiusOfTheOutermost;
    //文字左右padding
    private float mPaddingOfLR;
    //文字上下padding
    private float mPaddingOfTB;

    //画笔
    //边框线画笔
    private Paint mPaintOfSideLine;
    //文本画笔
    private Paint mPaintOfText;
    //拐角点画笔
    private Paint mPaintOfCorner;
    //环形画笔
    private Paint mPaintOfPolygon;
    //荧光效果画笔
    private Paint mPaintOfHighlighter;

    //动画
    private int alphaOfAverage;
    //第二组动画
    private AnimatorSet secondAnimatorSet;
    //第三组动画
    private AnimatorSet thirdAnimatorSet;
    private boolean startFirstAnimator;
    private boolean startSecondAnimator;
    //文字背景
    private Bitmap bgExplainBitmap;


    public RadarChart(Context context) {
        this(context, null);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initSize(context);
        initPaint(context);

    }

    private void initSize(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mRadiusOfTheInnermost = ConvertUtils.dp2pxF(context, 11);
        minSpaceOfLoopLine = ConvertUtils.dp2pxF(context, 9);
        mSpaceOfTextImg = ConvertUtils.dp2pxF(context, 8);
        mSizeOfTypeface = ConvertUtils.sp2pxF(context, 11);
        mOuterEdgeWidth = ConvertUtils.dp2pxF(context, 2);
        mInnerEdgeWidth = ConvertUtils.dp2pxF(context, 1);
        mPointSizeOfSquare = ConvertUtils.dp2px(context, 6);
        mPointSizeOfCircle = ConvertUtils.dp2pxF(context, 1f);
        mPaddingOfLR = ConvertUtils.dp2pxF(context, 15);
        mPaddingOfTB = ConvertUtils.dp2pxF(context, 8);

        mRadiusOfTheOutermost = mRadiusOfTheInnermost + minSpaceOfLoopLine * (mNumOfPlies - 1);
        float minTextWidth = ConvertUtils.dp2pxF(context, 100);
        minViewWidth = (float) ((mRadiusOfTheOutermost * Math.cos(ConvertUtils.degreeToRadian(18)) + mSpaceOfTextImg + minTextWidth) * 2);
        float minTextHeight = ConvertUtils.dp2px(context, 40);
        minViewHeight = (float) (mRadiusOfTheOutermost + mRadiusOfTheOutermost * Math.cos(ConvertUtils.degreeToRadian(36))
                + mSpaceOfTextImg * 2 + minTextHeight * 2 + mPaddingOfTB * 2);



    }

    private void initPaint(Context context) {
        mPaintOfSideLine = new Paint();
        mPaintOfSideLine.setAntiAlias(true);
        mPaintOfSideLine.setStrokeWidth(mOuterEdgeWidth);
        mPaintOfSideLine.setColor(COLOR_BORDERWHITE);
        mPaintOfSideLine.setStyle(Paint.Style.STROKE);

        mPaintOfText = new Paint();
        mPaintOfText.setColor(Color.WHITE);
        mPaintOfText.setTextSize(mSizeOfTypeface);
        mPaintOfText.setAntiAlias(true);

        mPaintOfPolygon = new Paint();
        mPaintOfPolygon.setAntiAlias(true);
        mPaintOfPolygon.setStyle(Paint.Style.FILL);
        mPaintOfPolygon.setColor(COLOR_REDAR);

        mPaintOfCorner = new Paint();
        mPaintOfCorner.setAntiAlias(true);
        mPaintOfCorner.setStyle(Paint.Style.FILL);

        mPaintOfHighlighter = new Paint();
        mPaintOfHighlighter.setAntiAlias(true);
        mPaintOfHighlighter.setStyle(Paint.Style.STROKE);
        mPaintOfHighlighter.setStrokeWidth(mOuterEdgeWidth);

        bgExplainBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_scoreresult_explain);

    }

    private void initData() {
        mVertexDataOfPerLayer = new HashMap<>();
        for (int i = 0; i < mNumOfPlies; i++) {
            List vertexList = new ArrayList<>();
            //当前五边形的半径
            float curRadius = mRadiusOfTheInnermost + minSpaceOfLoopLine * i;
            //音程
            Point point1 = new Point();
            point1.x = mPointOfCenter.x;
            point1.y = (int) (mPointOfCenter.y - curRadius);
            vertexList.add(point1);
            //稳定性
            Point point2 = new Point();
            point2.x = (int) (mPointOfCenter.x + curRadius * Math.cos(degreeToRadian(18)));
            point2.y = (int) (mPointOfCenter.y - curRadius * Math.sin(degreeToRadian(18)));
            vertexList.add(point2);
            //表现力
            Point point3 = new Point();
            point3.x = (int) (mPointOfCenter.x + curRadius * Math.sin(degreeToRadian(36)));
            point3.y = (int) (mPointOfCenter.y + curRadius * Math.cos(degreeToRadian(36)));
            vertexList.add(point3);
            //长音
            Point point4 = new Point();
            point4.x = (int) (mPointOfCenter.x - curRadius * Math.sin(degreeToRadian(36)));
            point4.y = (int) (mPointOfCenter.y + curRadius * Math.cos(degreeToRadian(36)));
            vertexList.add(point4);
            //节奏
            Point point5 = new Point();
            point5.x = (int) (mPointOfCenter.x - curRadius * Math.cos(degreeToRadian(18)));
            point5.y = (int) (mPointOfCenter.y - curRadius * Math.sin(degreeToRadian(18)));
            vertexList.add(point5);
            mVertexDataOfPerLayer.put(i, vertexList);
        }

        setAverageValue();
        setScoreValue();
        setInitialScoreValue();
        loadAnimation();
    }

    //设置平均值
    private void setAverageValue() {
        mVertexDataOfAverage = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int plies = new Random().nextInt(mNumOfPlies);
            int orientation = i;
            mVertexDataOfAverage.add(mVertexDataOfPerLayer.get(plies).get(orientation));
        }
    }

    //设置分数
    private void setScoreValue() {
        mVertexDataOfScore = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int plies = new Random().nextInt(mNumOfPlies);
            int orientation = i;
            mVertexDataOfScore.add(mVertexDataOfPerLayer.get(plies).get(orientation));
        }
    }

    //设置初始的分数值
    private void setInitialScoreValue() {
        List<Point> initList = mVertexDataOfPerLayer.get(1);
        mVertexDataOfInitScore = new ArrayList<>();
        mVertexDataOfInitScore.addAll(initList);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mViewWidth = Math.max(minViewWidth, widthSize);
        } else {
            mViewWidth = minViewWidth;
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = Math.max(minViewHeight, heightSize);
        } else {
            mViewHeight = minViewHeight;
        }

        setMeasuredDimension((int) mViewWidth, (int) mViewHeight);
        mPointOfCenter = new Point((int) mViewWidth / 2, (int) mViewHeight / 2);
        initData();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawPolygon(canvas);
        drawDiagonal(canvas);
        drawVertex(canvas);

        drawAverage(canvas);
        drawScore(canvas);

        drawExplain(canvas);
    }

    //绘制多边形
    private void drawPolygon(Canvas canvas) {
        canvas.save();
        for (int i = mVertexDataOfPerLayer.size() - 1; i >= 0; i--) {
            //准备路径
            List<Point> vertexList = mVertexDataOfPerLayer.get(i);
            Path path = new Path();
            for (int j = 0; j < vertexList.size(); j++) {
                Point point = vertexList.get(j);
                if (j == 0) {
                    path.moveTo(point.x, point.y);
                } else {
                    path.lineTo(point.x, point.y);
                }
            }
            path.close();
            //绘制多边形
            mPaintOfPolygon.setColor(COLOR_REDAR);
            canvas.drawPath(path, mPaintOfPolygon);

            //绘制边框
            if (i == mVertexDataOfPerLayer.size() - 1) {
                //绘制光晕
                mPaintOfHighlighter.setColor(COLOR_BORDERINDIGO);
                mPaintOfHighlighter.setMaskFilter(new BlurMaskFilter(ConvertUtils.dp2px(getContext(), 12),
                        BlurMaskFilter.Blur.SOLID));
                canvas.drawPath(path, mPaintOfHighlighter);

                mPaintOfSideLine.setColor(Color.WHITE);
                mPaintOfSideLine.setStrokeWidth(mOuterEdgeWidth);
                canvas.drawPath(path, mPaintOfSideLine);
            } else {
                mPaintOfSideLine.setColor(COLOR_BORDERINDIGO);
                mPaintOfSideLine.setStrokeWidth(mInnerEdgeWidth);
                canvas.drawPath(path, mPaintOfSideLine);
            }

        }

        canvas.restore();
    }

    //绘制对角线
    private void drawDiagonal(Canvas canvas) {
        canvas.save();
        mPaintOfSideLine.setColor(COLOR_BORDERINDIGO);
        mPaintOfSideLine.setStrokeWidth(mInnerEdgeWidth);

        List<Point> vertexList = mVertexDataOfPerLayer.get(mVertexDataOfPerLayer.size() - 1);
        for (int i = 0; i < vertexList.size(); i++) {
            Point point = vertexList.get(i);
            canvas.drawLine(mPointOfCenter.x, mPointOfCenter.y, point.x, point.y, mPaintOfSideLine);
        }

        canvas.restore();
    }

    //绘制顶点
    private void drawVertex(Canvas canvas) {
        canvas.save();

        for (int i = 0; i < mVertexDataOfPerLayer.size(); i++) {
            if (i == mVertexDataOfPerLayer.size() - 1)
                return;

            List<Point> vertexList = mVertexDataOfPerLayer.get(i);
            for (int j = 0; j < vertexList.size(); j++) {
                //绘制正方形
                Point point = new Point();
                point.x = mPointOfCenter.x;
                point.y = (int) (mPointOfCenter.y - mRadiusOfTheInnermost - minSpaceOfLoopLine * i);
                Point point1 = new Point();
                point1.x = point.x;
                point1.y = (int) (point.y - mPointSizeOfSquare / 2);
                Point point2 = new Point();
                point2.x = (int) (point.x + mPointSizeOfSquare / 2);
                point2.y = point.y;
                Point point3 = new Point();
                point3.x = point.x;
                point3.y = (int) (point.y + mPointSizeOfSquare / 2);
                Point point4 = new Point();
                point4.x = (int) (point.x - mPointSizeOfSquare / 2);
                point4.y = point.y;

                Path path = new Path();
                path.moveTo(point1.x, point1.y);
                path.lineTo(point2.x, point2.y);
                path.lineTo(point3.x, point3.y);
                path.lineTo(point4.x, point4.y);
                path.close();
                mPaintOfCorner.setColor(COLOR_BORDERINDIGO);
                canvas.drawPath(path, mPaintOfCorner);

                //绘制圆点
                mPaintOfCorner.setColor(Color.WHITE);
                canvas.drawCircle(point.x, point.y, mPointSizeOfCircle, mPaintOfCorner);
                canvas.rotate(72, mPointOfCenter.x, mPointOfCenter.y);
            }

        }
        canvas.restore();
    }

    //绘制平均值
    private void drawAverage(Canvas canvas) {
        if (!startFirstAnimator) {
            return;
        }

        canvas.save();

        //找到平均值对应的点

        //连接每个点
        Path path = new Path();
        for (int i = 0; i < mVertexDataOfAverage.size(); i++) {
            Point point = mVertexDataOfAverage.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        path.close();
        mPaintOfPolygon.setARGB(alphaOfAverage, 204, 128, 86);
        canvas.drawPath(path, mPaintOfPolygon);

        //绘制边框
        mPaintOfSideLine.setStrokeWidth(mInnerEdgeWidth);
        mPaintOfSideLine.setARGB(alphaOfAverage, 255, 255, 255);
        canvas.drawPath(path, mPaintOfSideLine);
        canvas.restore();
    }

    //绘制分数
    private void drawScore(Canvas canvas) {

        if (!startSecondAnimator) {
            return;
        }

        canvas.save();
        //找到平均值对应的点

        //连接每个点
        Path path = new Path();
        for (int i = 0; i < mVertexDataOfInitScore.size(); i++) {
            Point point = mVertexDataOfInitScore.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
            }
        }
        path.close();
        mPaintOfPolygon.setColor(COLOR_SCORE);
        canvas.drawPath(path, mPaintOfPolygon);

        //绘制边框
        mPaintOfSideLine.setStrokeWidth(mInnerEdgeWidth);
        mPaintOfSideLine.setColor(Color.WHITE);
        canvas.drawPath(path, mPaintOfSideLine);
        canvas.restore();
    }

    //绘制文本
    private void drawExplain(Canvas canvas) {
        canvas.save();

        List<Point> outermostList = mVertexDataOfPerLayer.get(mNumOfPlies - 1);
        Paint.FontMetrics fontMetrics = mPaintOfText.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;

        //音程
        Point point1 = outermostList.get(0);
        String pitchStr = getContext().getString(R.string.score_tone);
        float pitchFloat = mPaintOfText.measureText(pitchStr);
        float pitchX = point1.x - pitchFloat / 2;
        float pitchY = point1.y - mSpaceOfTextImg - mPaddingOfTB - fontMetrics.descent;

        drawImage(canvas, bgExplainBitmap,
                point1.x - mPaddingOfLR - pitchFloat / 2,
                point1.y - mSpaceOfTextImg - mPaddingOfTB * 2 - fontHeight,
                mPaddingOfLR * 2 + pitchFloat,
                mPaddingOfTB * 2 + fontHeight,
                0, 0);
        canvas.drawText(pitchStr, pitchX, pitchY, mPaintOfText);

        //安定性
        Point point2 = outermostList.get(1);
        String stabilityStr = getContext().getString(R.string.score_stability);
        float stabilityFloat = mPaintOfText.measureText(stabilityStr);
        float stabilityX = point2.x + mSpaceOfTextImg + mPaddingOfLR;
        float stabilityY = point2.y - mPaddingOfTB - fontMetrics.bottom;

        drawImage(canvas, bgExplainBitmap,
                point2.x + mSpaceOfTextImg,
                point2.y - mPaddingOfTB * 2 - fontHeight,
                mPaddingOfLR * 2 + stabilityFloat,
                mPaddingOfTB * 2 + fontHeight,
                0, 0);
        canvas.drawText(stabilityStr, stabilityX, stabilityY, mPaintOfText);

        //表现力
        Point point3 = outermostList.get(2);
        String expressiveStr = getContext().getString(R.string.score_expression);
        float expressiveFloat = mPaintOfText.measureText(expressiveStr);
        float expressiveX = point3.x + mSpaceOfTextImg + mPaddingOfLR;
        float expressiveY = point3.y + mPaddingOfTB - fontMetrics.top;

        drawImage(canvas, bgExplainBitmap,
                point3.x + mSpaceOfTextImg,
                point3.y,
                mPaddingOfLR * 2 + expressiveFloat,
                mPaddingOfTB * 2 + fontHeight,
                0, 0);
        canvas.drawText(expressiveStr, expressiveX, expressiveY, mPaintOfText);

        //长音
        Point point4 = outermostList.get(3);
        String longsoundStr = getContext().getString(R.string.score_longsound);
        float longSoundFloat = mPaintOfText.measureText(longsoundStr);
        float longSoundX = point4.x - mSpaceOfTextImg - mPaddingOfLR - longSoundFloat;
        float longSoundY = point4.y + mPaddingOfTB - fontMetrics.top;

        drawImage(canvas, bgExplainBitmap,
                point4.x - mSpaceOfTextImg - mPaddingOfLR * 2 - longSoundFloat,
                point4.y,
                mPaddingOfLR * 2 + longSoundFloat,
                mPaddingOfTB * 2 + fontHeight,
                0, 0);
        canvas.drawText(longsoundStr, longSoundX, longSoundY, mPaintOfText);

        //节奏
        Point point5 = outermostList.get(4);
        String trillStr = getContext().getString(R.string.score_trill);
        float trillFloat = mPaintOfText.measureText(trillStr);
        float trillX = point5.x - mSpaceOfTextImg - mPaddingOfLR - trillFloat;
        float trillY = point5.y - mPaddingOfTB - fontMetrics.descent;

        drawImage(canvas, bgExplainBitmap,
                point5.x - mSpaceOfTextImg - mPaddingOfLR * 2 - trillFloat,
                point5.y - mPaddingOfTB * 2 - fontHeight,
                mPaddingOfLR * 2 + trillFloat,
                mPaddingOfTB * 2 + fontHeight,
                0, 0);
        canvas.drawText(trillStr, trillX, trillY, mPaintOfText);

        canvas.restore();
    }

    //加载动画
    private void loadAnimation() {
        startFirstAnimator = false;
        startSecondAnimator = false;

        //第一组动画:平均值渐显
        alphaOfAverage = 0;
        ValueAnimator animatorOfAverage = ValueAnimator.ofInt(0, 238);
        animatorOfAverage.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                alphaOfAverage = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });


        //第二组动画：五个点放大
        List<Animator> secondValueAnimators = new ArrayList<>();
        for (int i = 0; i < mVertexDataOfInitScore.size(); i++) {
            if (i == 0) {
                mVertexDataOfInitScore.set(0, mVertexDataOfScore.get(0));
            }
            secondValueAnimators.add(createAnimator(i, mPointOfCenter, mVertexDataOfInitScore.get(i)));
        }

        //第三组动画：依次放大其它四个顶点
        final List<Animator> thirdValueAnimators = new ArrayList<>();
        for (int i = 1; i < mVertexDataOfInitScore.size(); i++) {
            thirdValueAnimators.add(createAnimator(i, mVertexDataOfInitScore.get(i), mVertexDataOfScore.get(i)));
        }

        //播放动画
        thirdAnimatorSet = new AnimatorSet();
        thirdAnimatorSet.playSequentially(thirdValueAnimators);
        thirdAnimatorSet.setDuration(2500);

        secondAnimatorSet = new AnimatorSet();
        secondAnimatorSet.playTogether(secondValueAnimators);
        secondAnimatorSet.setDuration(1000);
        secondAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                startSecondAnimator = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                thirdAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animatorOfAverage.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                startFirstAnimator = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                secondAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorOfAverage.setDuration(1500);
        animatorOfAverage.start();

    }

    //创建动画
    private ValueAnimator createAnimator(final int position, Point startPoint, Point endPoint) {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mVertexDataOfInitScore.set(position, (Point) valueAnimator.getAnimatedValue());
                invalidate();
            }
        });
        return valueAnimator;
    }

    private void drawImage(Canvas canvas, Bitmap blt, float x, float y,
                           float w, float h, float bx, float by) {
        RectF src = new RectF();// 图片 >>原矩形
        RectF dst = new RectF();// 屏幕 >>目标矩形

        src.left = bx;
        src.top = by;
        src.right = bx + w;
        src.bottom = by + h;

        dst.left = x;
        dst.top = y;
        dst.right = x + w;
        dst.bottom = y + h;
        // 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
        // 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
        canvas.drawBitmap(blt, null, dst, null);
        src = null;
        dst = null;
    }

}
