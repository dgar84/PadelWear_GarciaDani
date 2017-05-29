package com.example.padelwear;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 26/05/2017.
 */

public class PortadaAnimacion extends Activity {

    private AnimatorSet set;
    private ImageView img_ball;
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.padel_splash);

        img_ball = (ImageView) findViewById(R.id.img_ball);
        set=(AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.animacion_portada);
        set.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                set.cancel();
                Intent mainIntent = new Intent().setClass(
                        PortadaAnimacion.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000);

      /*  DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        AnimatorSet set = new AnimatorSet();
        set.setDuration(1000);
        set.playTogether(
                ObjectAnimator.ofFloat(img_ball, "translationX", 0, 50),
                ObjectAnimator.ofFloat(img_ball, "translationY", 0, heightPixels));
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                Log.d("Animacion","animacion 1");
                AnimatorSet set2 = new AnimatorSet();
                set2.setDuration(1000);
                set2.playTogether(
                        ObjectAnimator.ofFloat(img_ball, "translationX", 50, 100),
                        ObjectAnimator.ofFloat(img_ball, "translationY", heightPixels, heightPixels - 50));
                set2.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.d("Animacion","animacion 2");
                        Intent mainIntent = new Intent().setClass(
                                PortadaAnimacion.this, MainActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }); */
    }
}
