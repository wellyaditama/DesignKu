package com.amikom.desainku.utility;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amikom.desainku.R;

public class AnimationManagerClass {

    public static void clockwise(View view, Context context){

        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.clockwise);
        view.startAnimation(animation);
    }

    public static void zoom(View view, Context context){
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.myanimation);
        view.startAnimation(animation);
    }

    public static void fade(View view, Context context){
        Animation animation1 =
                AnimationUtils.loadAnimation(context,
                        R.anim.fade);
        view.startAnimation(animation1);
    }

    public static void move(View view, Context context){
        Animation animation1 =
                AnimationUtils.loadAnimation(context, R.anim.move);
        view.startAnimation(animation1);
    }

    public static void slide(View view, Context context){
        Animation animation1 =
                AnimationUtils.loadAnimation(context, R.anim.slide);
        view.startAnimation(animation1);
    }
}
