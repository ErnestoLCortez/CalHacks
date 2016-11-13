package com.example.android.gamecontroller;

import android.app.ActionBar;
import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import co.tanvas.haptics.service.app.*;
import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.model.*;

import montebaes.tanvas.controls.ButtonDragListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /* Log Declaration */
    final String LOG_MODE = "MODE CHANGE : ";

    /* MODE */
    private static boolean MODE = false;

    ImageButton temp;

    /* Content */
    TextView modeType;
    Button dragDropButton;

    ImageButton redButton, greenButton;

    ImageView testMoveImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dragDropButton = (Button) findViewById(R.id.drag_drop_button);
        dragDropButton.setOnClickListener(this);

        //testMoveImg = (ImageView) findViewById(R.id.test_move_img);
        //testMoveImg = (ImageView) findViewById(R.id.button_red_unpressed);
        //testMoveImg.setOnClickListener(this);

        modeType = (TextView) findViewById(R.id.mode_type);
        modeType.setOnClickListener(this);
        modeType.setText(R.string.mode_false);

        greenButton = (ImageButton) findViewById(R.id.button_green);
        redButton = (ImageButton) findViewById(R.id.button_red);
        redButton.setOnClickListener(this);
        redButton.setImageResource(R.drawable.redb);

        findViewById(R.id.activity_main).setOnDragListener(new ButtonDragListener());

    }

    public void onClick(View v){
        //Drag and Drop Button
        if(v.getId() == R.id.drag_drop_button) {
            if (MODE) {
                changeMODE(false);
                modeType.setText(R.string.mode_false);
                unSetAllLongClickListener();

            } else {
                changeMODE(true);
                modeType.setText(R.string.mode_true);

                /* START DRAG/DROP MODE */
                temp = null;
                setAllLongClickListener();
            }
        }
//        else if(v.getId() == redButton.getId()){
//            if(redButton.getDrawable().equals(R.drawable.greena)) {
//                redButton.setImageResource(R.drawable.redb);
//            }
//            else{
//                redButton.setImageResource(R.drawable.greena);
//            }
//        }
    }

    //Only set on long click listener when this function is called
    public void setAllLongClickListener(){
        redButton.setOnLongClickListener(longListen);
        greenButton.setOnLongClickListener(longListen);

    }

    public void unSetAllLongClickListener(){
        redButton.setOnLongClickListener(null);
        greenButton.setOnLongClickListener(null);
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

        public ImageButton var;

        public DragShadow(View view){
            super(view);
            var = (ImageButton) view;
        }

        @Override
        public void onDrawShadow(Canvas canvas){
            //Canvas is the area of the shadow
            //temp.setImageResource(R.drawable.black);
            var.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){
            View v = getView();

            int height = (int) v.getHeight();
            int width = (int) v.getWidth();


            shadowSize.set(width, height);

            //Drag exactly in the center
            shadowTouchPoint.set((int)width/2, (int)height/2);
        }
    }


}

