package com.example.dkovalev.waveproject.Assets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.example.dkovalev.waveproject.Assets.Sector;
import com.example.dkovalev.waveproject.Assets.WaterContainerDataModel;

/**
 * Created by ardmn on 18.12.2015.
 */
public class WaterCurtain extends View {

    private Sector sector = new Sector(Color.WHITE, 0f);
    private Paint paint = new Paint();
    private WaterContainerDataModel.WaterContainerIcons waterContainerIcons;


    public WaterCurtain(Context context) {
        super(context);
    }

    public WaterCurtain(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterContainerDataModel.WaterContainerIcons getWaterContainerIcons() {
        return waterContainerIcons;
    }

    public void setWaterContainerIcons(WaterContainerDataModel.WaterContainerIcons waterContainerIcons) {
        this.waterContainerIcons = waterContainerIcons;
    }

    public WaterCurtain(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaterCurtain(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        paint.setColor(sector.getColor());

        float topOffset = getStandardOffsetTop();
        float bottomOffset = getStandardOffsetBottom();
        float scaleY = getImageScaleY(h);
        if (scaleY != 0) {
            topOffset *= scaleY;
            bottomOffset *= scaleY;
        }

        float h_WaterImageContent = h - (topOffset + bottomOffset);

        float curtainHeightFromTop = h_WaterImageContent - h_WaterImageContent * sector.getPercent();
        canvas.drawRect(0, 0, w, topOffset + curtainHeightFromTop, paint);

    }

    private float getImageScaleY(float viewHeight) {
        float result = 0;

        if (waterContainerIcons != null)
            result = viewHeight / waterContainerIcons.getOriginalImgHeight();


        return result;
    }

    private float getStandardOffsetTop() {
        float result = 0;
        if (waterContainerIcons != null)
            result = waterContainerIcons.getTopContentOffset();

        return result;
    }

    private float getStandardOffsetBottom() {
        float result = 0;
        if (waterContainerIcons != null)
            result = waterContainerIcons.getBottomContentOffset();

        return result;
    }
}
