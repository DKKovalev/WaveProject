/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.gelitenight.waveview.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class WaveView extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#280000FF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3C0000FF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE,
        BOTTLE,
        DROP,
        GLASS
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       <br/>Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        <br/>Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       <br/>Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        <br/>Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        // need to recreate shader when color changed
        mWaveShader = null;
        createShader();
        invalidate();
    }

    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                                (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
                    if (borderWidth > 0) {
                        canvas.drawRect(
                                borderWidth / 2f,
                                borderWidth / 2f,
                                getWidth() - borderWidth / 2f - 0.5f,
                                getHeight() - borderWidth / 2f - 0.5f,
                                mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                            getHeight() - borderWidth, mViewPaint);
                    break;
                case BOTTLE:
// 80
                    drawScalableBottle(canvas, 135, 260);
                    //drawBottle(canvas);

                    break;
                case DROP:

                    drawDrop(canvas);

                    break;
                case GLASS:
                    drawGlass(canvas);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("TAG_HEIGHT", String.valueOf(MeasureSpec.getSize(heightMeasureSpec)));
        Log.d("TAG_WIDTH", String.valueOf(MeasureSpec.getSize(widthMeasureSpec)));
    }

    private void drawBottle(Canvas canvas) {
        Path path = new Path();

        mViewPaint.setStyle(Style.FILL_AND_STROKE);
        path.moveTo(getWidth() / 2 + 50, getTop() + 20);
        path.lineTo(getWidth() / 2 - 45, getTop() + 20); //1-2
        path.quadTo(getWidth() / 2 - 160, getTop() + 265, getWidth() / 2 - 136, 320); //2-3
        path.lineTo(getWidth() / 2 - 135, getTop() + 700); //3-7

        path.quadTo(getWidth() / 2 - 110, getTop() + 762, getWidth() / 2 - 60, getTop() + 700); //7-8
        path.quadTo(getWidth() / 2, getTop() + 762, getWidth() / 2 + 60, getTop() + 700); //8-9
        path.quadTo(getWidth() / 2 + 110, getTop() + 762, getWidth() / 2 + 138, getTop() + 700); //9-10

        path.lineTo(getWidth() / 2 + 138, getTop() + 320); //10-14
        path.quadTo(getWidth() / 2 + 160, getTop() + 265, getWidth() / 2 + 50, getTop() + 25); //14-1
        canvas.drawPath(path, mViewPaint);

        canvas.drawCircle(getWidth() / 2 - 135, getTop() + 260, 5, mViewPaint);
        canvas.drawCircle(getWidth() / 2 + 135, getTop() + 260, 5, mViewPaint);

        canvas.drawPath(path, mViewPaint);
    }

    private void drawScalableBottle(Canvas canvas, int x, int y) {
        Path path = new Path();

        float halfWidth = getWidth() / 2;

        float scalableWidthPlus45 = (float) (halfWidth / 0.9146341);
        float scalableWidthMinus40 = (float) (halfWidth / 1.117403);
        float scalableWidthMinus158 = (float) (halfWidth / 1.785714);
        float scalableWidthMinus136 = (float) (halfWidth / 1.569038);
        float scalableWidthPlus160 = (float) (halfWidth / 0.7009346);
        float scalableWidthMinus60 = (float) (halfWidth / 1.190476);
        float scalableWidthPlus60 = (float) (halfWidth / 0.862069);
        float scalableWidthMinus110 = (float) (halfWidth / 1.415094);
        float scalableWidthPlus110 = (float) (halfWidth / 0.7731959);
        float scalableWidthPlus138 = (float) (halfWidth / 0.7309942);

        float scalableHeight20 = (float) (getHeight() / 37.5);
        float scalableHeight200 = (float) (getHeight() / 3.75);
        float scalableHeight265 = (float) (getHeight() / 2.830186);
        float scalableHeight320 = (float) (getHeight() / 2.34375);
        float scalableHeight700 = (float) (getHeight() / 1.071429);
        float scalableHeight762 = (float) (getHeight() / 0.984252);

        mViewPaint.setStyle(Style.FILL_AND_STROKE);
        path.moveTo(scalableWidthPlus45, scalableHeight20);
        path.lineTo(scalableWidthMinus40, scalableHeight20); //1-2
        path.quadTo(scalableWidthMinus158, scalableHeight265, scalableWidthMinus136, scalableHeight320); //2-3
        path.lineTo(scalableWidthMinus136, scalableHeight700); //3-7

        path.quadTo(scalableWidthMinus110, scalableHeight762, scalableWidthMinus60, scalableHeight700); //7-8
        path.quadTo(getWidth() / 2, scalableHeight762, scalableWidthPlus60, scalableHeight700); //8-9
        path.quadTo(scalableWidthPlus110, scalableHeight762, scalableWidthPlus138, scalableHeight700); //9-10

        path.lineTo(scalableWidthPlus138, scalableHeight320); //10-14
        path.quadTo(scalableWidthPlus160, scalableHeight200, scalableWidthPlus45, scalableHeight20); //14-1
        canvas.drawPath(path, mViewPaint);

        canvas.drawCircle(getWidth() / 2 - x, getTop() + y, 5, mViewPaint);
        canvas.drawCircle(getWidth() / 2 + x, getTop() + y, 5, mViewPaint);
    }

    private void drawDrop(Canvas canvas) {
        Path path = new Path();

        float halfWidth = getWidth() / 2;

        float scalableWidthMinus200 = (float) (halfWidth / 2.5);
        float scalableWidthMinus5 = (float) (halfWidth / 1.013514);
        float scalableWidthPlus5 = (float) (halfWidth / 0.9868421);
        float scalableWidthMinus230 = (float) (halfWidth / 2.586207);
        float scalableWidthMinus150 = (float) (halfWidth / 1.666667);
        float scalableWidthPlus150 = (float) (halfWidth / 0.7009346);
        float scalableWidthPlus225 = (float) (halfWidth / 0.625);

        float scalableHeight20 = (float) (getHeight() / 37.5);
        float scalableHeight40 = (float) (getHeight() / 18.75);
        float scalableHeight400 = (float) (getHeight() / 1.875);
        float scalableHeight475 = (float) (getHeight() / 1.578947);
        float scalableHeight748 = (float) (getHeight() / 1.002674);
        float scalableHeight700 = (float) (getHeight() / 1.071429);
        float scalableHeight740 = (float) (getHeight() / 1.013514);

        mViewPaint.setStyle(Style.FILL_AND_STROKE);
        path.moveTo(halfWidth, scalableHeight20);
        path.lineTo(scalableWidthMinus5, scalableHeight40);
        path.lineTo(scalableWidthMinus200, scalableHeight400);
        path.lineTo(scalableWidthMinus230, scalableHeight475);
        path.quadTo(scalableWidthMinus150, scalableHeight748, halfWidth, scalableHeight700);
        path.quadTo(scalableWidthPlus150, scalableHeight740, scalableWidthPlus225, scalableHeight475);
        path.lineTo(scalableWidthPlus225, scalableHeight400);
        path.lineTo(scalableWidthPlus5, scalableHeight40);
        path.lineTo(halfWidth, 20);
        canvas.drawPath(path, mViewPaint);
    }

    private void drawGlass(Canvas canvas) {
        Path path = new Path();

        float halfWidth = getWidth() / 2;

        float scalableWidthMinus240 = (float) (halfWidth / 2.777778);
        float scalableWidthPlus240 = (float) (halfWidth / 0.6097561);
        float scalableWidthMinus193 = (float) (halfWidth / 2.06044);
        float scalableWidthPlus193 = (float) (halfWidth / 0.6602113);

        float scalableHeight80 = (float) (getHeight() / 9.375);
        float scalableHeight700 = (float) (getHeight() / 1.111111);

        mViewPaint.setStyle(Style.FILL_AND_STROKE);
        path.moveTo(halfWidth, scalableHeight80);
        path.lineTo(scalableWidthMinus240, scalableHeight80);
        path.lineTo(scalableWidthMinus193-1, scalableHeight700);
        path.lineTo(scalableWidthPlus193, scalableHeight700);
        path.lineTo(scalableWidthPlus240, scalableHeight80);
        path.lineTo(halfWidth, scalableHeight80);
        canvas.drawPath(path, mViewPaint);
    }
}
