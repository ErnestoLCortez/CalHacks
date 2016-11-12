package com.example.android.gamecontroller;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /* Log Declaration */
    final String LOG_MODE = "MODE CHANGE : ";

    /* MODE */
    private static boolean MODE = false;

    /* Content */
    TextView modeType;
    Button dragDropButton;
    ImageView testMoveImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dragDropButton = (Button) findViewById(R.id.drag_drop_button);
        dragDropButton.setOnClickListener(this);

        testMoveImg = (ImageView) findViewById(R.id.test_move_img);
        testMoveImg.setOnClickListener(this);

        modeType = (TextView) findViewById(R.id.mode_type);
        modeType.setOnClickListener(this);
        modeType.setText(R.string.mode_false);

        testMoveImg.setOnLongClickListener(longListen);
    }

    public void onClick(View v){
        //Drag and Drop Button
        if(v.getId() == R.id.drag_drop_button) {
            if (MODE) {
                changeMODE(false);
                modeType.setText(R.string.mode_false);
            } else {
                changeMODE(true);
                modeType.setText(R.string.mode_true);

                /* START DRAG/DROP MODE */
                setAllLongClickListener();
            }
        }
    }

    //Only set on long click listener when this function is called
    public void setAllLongClickListener(){
        testMoveImg.setOnLongClickListener(longListen);
    }

    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("","");

            DragShadow dragShadow = new DragShadow(view);

            view.startDrag(data, dragShadow, view, 0);
            return false;
        }
    };



    public void changeMODE(boolean MODE){
        this.MODE = MODE;
        Log.v(LOG_MODE, String.valueOf(MODE));
    }

    private class DragShadow extends View.DragShadowBuilder{

        public DragShadow(View view){
            super(view);
        }

        @Override
        public void onDrawShadow(Canvas canvas){
            //Canvas is the area of the shadow
            testMoveImg.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){
            View v = getView();

            int height = (int)v.getHeight()/2;
            int width = (int)v.getWidth()/2;

            shadowSize.set(width, height);

            //Drag exactly in the center
            shadowTouchPoint.set((int)width/2, (int)height/2);
        }

    }
}
