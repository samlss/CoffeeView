package com.iigo.library;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SamLeung
 * @e-mail samlssplus@gmail.com / 729717222@qq.com
 * @github https://github.com/samlss
 * @description A coffee view.
 */
public class CoffeeView extends View {
    private final static int DEFAULT_SIZE = 250; //200 px
    private final static int VAPOR_COUNT = 3;

    private final static int DEFAULT_CUP_COLOR = Color.parseColor("#91785e");
    private final static int DEFAULT_COASTER_COLOR = Color.parseColor("#999386");
    private final static int DEFAULT_VAPOR_COLOR = Color.parseColor("#938a80");

    private int mCupColor = DEFAULT_CUP_COLOR;
    private int mCoasterColor = DEFAULT_COASTER_COLOR;
    private int mVaporColor = DEFAULT_VAPOR_COLOR;

    private Paint mCupPaint;
    private Paint mCupEarPaint;
    private Paint mCoasterPaint;
    private Paint mVaporPaint;

    private Path mCupPath;
    private Path mCupEarPath;
    private Path mCoasterPath;

    private Path[] mVaporsPath = new Path[VAPOR_COUNT];
    private Path mCalculatePath;
    private Matrix mCalculateMatrix;
    private AnimatorSet mAnimatorSet;
    private List<Animator> mValueAnimators;
    private float[] mAnimatorValues;

    private float mVaporsHeight, mCenterX, mCenterY;

    public CoffeeView(Context context) {
        this(context, null);
    }

    public CoffeeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoffeeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        parseAttrs(attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CoffeeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        parseAttrs(attrs);
        init();
    }

    private void parseAttrs(AttributeSet attrs){
        if (attrs == null){
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CoffeeView);
        mCupColor = typedArray.getColor(R.styleable.CoffeeView_cupColor, DEFAULT_CUP_COLOR);
        mCoasterColor = typedArray.getColor(R.styleable.CoffeeView_coasterColor, DEFAULT_COASTER_COLOR);
        mVaporColor = typedArray.getColor(R.styleable.CoffeeView_vaporColor, DEFAULT_VAPOR_COLOR);
        typedArray.recycle();
    }

    private void init(){
        mCupPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCupPaint.setStyle(Paint.Style.FILL);
        mCupPaint.setColor(mCupColor);

        mCupEarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCupEarPaint.setStyle(Paint.Style.STROKE);
        mCupEarPaint.setColor(mCupColor);

        mCoasterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoasterPaint.setStyle(Paint.Style.FILL);
        mCoasterPaint.setColor(mCoasterColor);

        mVaporPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVaporPaint.setStyle(Paint.Style.STROKE);
        mVaporPaint.setColor(mVaporColor);

        mCupPath = new Path();
        mCupEarPath = new Path();
        mCoasterPath = new Path();

        for (int i = 0; i < VAPOR_COUNT; i++){
            mVaporsPath[i] = new Path();
        }

        mCalculatePath = new Path();
        mCalculateMatrix = new Matrix();

        mValueAnimators = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int w = widthSpecSize;
        int h = heightSpecSize;

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            w = DEFAULT_SIZE;
            h = DEFAULT_SIZE;
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            w = DEFAULT_SIZE;
            h = heightSpecSize;
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            w = widthSpecSize;
            h = DEFAULT_SIZE;
        }

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //cup part
        mCenterX = w / 2;
        mCenterY = h / 2;

        float cupWidth  = w * 2 / 3f;
        float cupHeight = (h / 2) * 3 / 4f;

        float cupCenterX = mCenterX;
        float cupCenterY = mCenterY + cupHeight / 2;

        float cupTopRoundRadius = Math.min(cupWidth, cupHeight) / 20f;
        float cupBottomRoundRadius = cupTopRoundRadius * 10;

        mCupPath.reset();
        mCupPath.addRoundRect(new RectF(cupCenterX - cupWidth / 2, cupCenterY - cupHeight / 2 - cupHeight / 10, cupCenterX + cupWidth / 2, cupCenterY + cupHeight / 2),
                new float[]{cupTopRoundRadius, cupTopRoundRadius, cupTopRoundRadius, cupTopRoundRadius,
                        cupBottomRoundRadius, cupBottomRoundRadius, cupBottomRoundRadius, cupBottomRoundRadius}, Path.Direction.CW);


        float cupEarWidth  = (w - cupWidth) * 3 / 4f;
        float cupEarHeight = cupHeight / 3;

        float cupEarCenterX = mCenterX + cupWidth / 2;
        float cupEarCenterY = mCenterY + cupHeight / 2;

        float cupEarRoundRadius = Math.min(cupEarWidth, cupEarHeight) / 2f;

        mCupEarPaint.setStrokeWidth(Math.min(cupEarWidth, cupEarHeight) / 3f);
        mCupEarPath.reset();
        mCupEarPath.addRoundRect(new RectF(cupEarCenterX - cupEarWidth / 2, cupEarCenterY - cupEarHeight / 2 - cupHeight / 10, cupEarCenterX + cupEarWidth / 2,
                        cupEarCenterY + cupEarHeight / 2),
                new float[]{cupEarRoundRadius, cupEarRoundRadius, cupEarRoundRadius, cupEarRoundRadius,
                        cupEarRoundRadius, cupEarRoundRadius, cupEarRoundRadius, cupEarRoundRadius}, Path.Direction.CW);


        //coaster part
        float coasterWidth = cupWidth;
        float coasterHeight = (h / 2 - cupHeight) * 1 / 3f;

        float coasterCenterX = mCenterX;
        float coasterCenterY = mCenterY + cupHeight + (h / 2 - cupHeight) / 2f;

        float coasterRoundRadius = Math.min(coasterWidth, coasterHeight) / 2f;

        mCoasterPath.reset();
        mCoasterPath.addRoundRect(new RectF(coasterCenterX - coasterWidth / 2, coasterCenterY - coasterHeight / 2,
                        coasterCenterX + coasterWidth / 2, coasterCenterY + coasterHeight / 2),
                coasterRoundRadius, coasterRoundRadius, Path.Direction.CW);

        //the vapors part
        float vaporsStrokeWidth = cupWidth / 15f;
        float vaporsGapWidth = (cupWidth - VAPOR_COUNT * vaporsStrokeWidth) / 4f;
        mVaporsHeight   = cupHeight * 4 / 5f;

        mVaporPaint.setStrokeWidth(vaporsStrokeWidth);
        float startX, startY, stopX, stopY;

        LinearGradient linearGradient = new LinearGradient(mCenterX, mCenterY, mCenterX, mCenterY - mVaporsHeight,
                new int[]{mVaporColor, Color.TRANSPARENT},
                null, Shader.TileMode.CLAMP);

        mVaporPaint.setShader(linearGradient);

        for (int i = 0; i < VAPOR_COUNT; i++) {
            mVaporsPath[i].reset();

            startX = (mCenterX - cupWidth / 2) + vaporsStrokeWidth / 2 + i * vaporsStrokeWidth + (i + 1) * vaporsGapWidth;
            startY = mCenterY + mVaporsHeight;

            stopX = startX;
            stopY = mCenterY - mVaporsHeight;


            mVaporsPath[i].moveTo(startX, startY);
            mVaporsPath[i].quadTo(startX - vaporsGapWidth / 2, startY - mVaporsHeight / 4,
                    startX, startY - mVaporsHeight / 2);

            mVaporsPath[i].quadTo(startX + vaporsGapWidth / 2, startY - mVaporsHeight * 3 / 4,
                    startX, mCenterY);

            mVaporsPath[i].quadTo(startX - vaporsGapWidth / 2, mCenterY - mVaporsHeight / 4,
                    startX, mCenterY - mVaporsHeight / 2);

            mVaporsPath[i].quadTo(startX + vaporsGapWidth / 2, mCenterY - mVaporsHeight * 3 / 4,
                    stopX, stopY);

            //add twice the bezier curve
            mVaporsPath[i].quadTo(startX - vaporsGapWidth / 2, stopY - mVaporsHeight / 4,
                    startX, stopY - mVaporsHeight / 2);

            mVaporsPath[i].quadTo(startX + vaporsGapWidth / 2, stopY - mVaporsHeight * 3 / 4,
                    startX, stopY - mVaporsHeight);

            mVaporsPath[i].quadTo(startX - vaporsGapWidth / 2, stopY - mVaporsHeight - mVaporsHeight / 4,
                    startX, stopY - mVaporsHeight - mVaporsHeight / 2);

            mVaporsPath[i].quadTo(startX + vaporsGapWidth / 2, stopY - mVaporsHeight - mVaporsHeight * 3 / 4,
                    stopX, stopY - 2 * mVaporsHeight);
        }

        setupAnimators(mVaporsHeight);
        start();
    }

    private void setupAnimators(float vaporHeight){
        release();
        mAnimatorValues = new float[VAPOR_COUNT];

        for (int i = 0; i < VAPOR_COUNT; i++) {
            mAnimatorValues[i] = 0f;

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, -vaporHeight);
            final int finalI = i;
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mAnimatorValues[finalI] = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });

            valueAnimator.setDuration(1000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimators.add(valueAnimator);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawVapors(canvas);
        drawCup(canvas);
        drawCoaster(canvas);
    }

    private void drawVapors(Canvas canvas){
        for (int i = 0; i < VAPOR_COUNT; i++){
            mCalculateMatrix.reset();
            mCalculatePath.reset();

            float animatedValue = mAnimatorValues[i];
            mCalculateMatrix.postTranslate(0, animatedValue);
            mVaporsPath[i].transform(mCalculateMatrix, mCalculatePath);

            canvas.drawPath(mCalculatePath, mVaporPaint);
        }
    }

    private void drawCup(Canvas canvas){
        canvas.drawPath(mCupEarPath, mCupEarPaint);
        canvas.drawPath(mCupPath, mCupPaint);
    }

    private void drawCoaster(Canvas canvas){
        canvas.drawPath(mCoasterPath, mCoasterPaint);
    }

    /**
     * Start animation.
     * */
    public void start(){
        stop();

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mValueAnimators);
        mAnimatorSet.start();
    }

    /**
     * Stop animation.
     * */
    public void stop(){
        if (mAnimatorSet != null && mAnimatorSet.isRunning()){
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    /**
     * When you do not need to use this, you should better to call this method to release.
     * */
    public void release(){
        stop();
        for (Animator animator : mValueAnimators){
            ((ValueAnimator)animator).removeAllUpdateListeners();
        }

        mValueAnimators.clear();
    }


    /**
     * Get the color of the cup.
     * */
    public int getCupColor() {
        return mCupColor;
    }

    /**
     * Set the color of the cup.
     * */
    public void setCupColor(int cupColor) {
        this.mCupColor = cupColor;
        mCupPaint.setColor(mCupColor);
        mCupEarPaint.setColor(mCupColor);
        postInvalidate();
    }

    /**
     * Get the color of the coaster.
     * */
    public int getCoasterColor() {
        return mCoasterColor;
    }

    /**
     * Set the color of the coaster.
     * */
    public void setCoasterColor(int coasterColor) {
        this.mCoasterColor = coasterColor;
        mCoasterPaint.setColor(mCoasterColor);
        postInvalidate();
    }

    /**
     * Get the color of the vapors.
     * */
    public int getVaporColor() {
        return mVaporColor;
    }

    /**
     * Set the color of the vapors.
     * */
    public void setVaporColor(int vaporColor) {
        this.mVaporColor = vaporColor;

        LinearGradient linearGradient = new LinearGradient(mCenterX, mCenterY, mCenterX, mCenterY - mVaporsHeight,
                new int[]{mVaporColor, Color.TRANSPARENT},
                null, Shader.TileMode.CLAMP);
        mVaporPaint.setColor(mVaporColor);
        mVaporPaint.setShader(linearGradient);
        postInvalidate();
    }
}
