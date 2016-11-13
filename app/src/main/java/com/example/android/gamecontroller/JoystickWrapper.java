package com.example.android.gamecontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by atomi on 11/12/2016.
 */

public class JoystickWrapper extends RelativeLayout{

    public JoystickWrapper(Context context){
        super(context);
    }

    public JoystickWrapper(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public JoystickWrapper(Context context, AttributeSet attrs, int x){
        super(context, attrs, x);
    }

    public JoystickWrapper(Context context, AttributeSet attrs, int x, int y){
        super(context, attrs, x, y);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//
//
//        return !(ev.getAction() == MotionEvent.ACTION_UP);
//
//    }
}