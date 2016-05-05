package com.example.dkovalev.waveproject.Assets;

import android.graphics.Color;

/**
 * Created by MrLero on 23.04.2015.
 */
public class Sector {
    int color = 0;
    float percent = 0;
    int degrees = 0;

    public Sector() {
    }

    public Sector(int color, float percent) {
        this.color = color;
        this.percent = percent;

        throwException();
    }

    public Sector(int color, float percent, int degrees) {
        this.color = color;
        this.percent = percent;
        this.degrees = degrees;

        throwException();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;

        throwException();
    }

    int getDegrees() {
        return degrees;
    }

    void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public Sector(String color, String percent) {
        this.color = Color.parseColor(color);
        this.percent = Float.valueOf(percent);

        throwException();
    }

    protected void throwException() {
        if (this.percent < 0.0f || this.percent > 1.0f)
            throw new IllegalArgumentException("число процентов должно быть в интревале [0,1]");
    }

    public Sector clone() {
        return new Sector(color, percent, degrees);
    }
}
