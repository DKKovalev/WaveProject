package com.example.dkovalev.waveproject.Assets;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

import com.gelitenight.waveview.library.WaveView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.kovalev on 28.04.2016.
 */
public class WaveHelper {
    private WaveView waveView;

    private AnimatorSet animatorSet;

    public WaveHelper(WaveView waveView) {
        this.waveView = waveView;
        initAnimation();
    }

    public void start() {
        waveView.setShowWave(true);
        if (animatorSet != null) {
            animatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animatorList = new ArrayList<>();

        ObjectAnimator waveShiftAnimation = ObjectAnimator.ofFloat(waveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnimation.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnimation.setDuration(1000);
        waveShiftAnimation.setInterpolator(new LinearInterpolator());
        animatorList.add(waveShiftAnimation);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
    }

    public void cancel() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }
}
