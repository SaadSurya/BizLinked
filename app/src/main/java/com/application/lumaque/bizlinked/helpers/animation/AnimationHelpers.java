package com.application.lumaque.bizlinked.helpers.animation;

import android.animation.Animator;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


public class AnimationHelpers {

    public static void animation(Techniques techniques, int duration, final View view) {
        YoYo.with(techniques)
                .duration(duration)
                .repeat(0)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE)
                            view.setVisibility(View.VISIBLE);
                    }
                }).playOn(view);
    }

    public static void animation(Techniques techniques, int duration, int repeat, final View view) {
        //view.clearAnimation();
        // view.clearFocus();
        YoYo.with(techniques)
                .onStart(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE)
                            view.setVisibility(View.VISIBLE);
                    }
                })
                .duration(duration)
                .repeat(repeat)
                .playOn(view);
        view.clearFocus();
        view.clearAnimation();
    }
}
