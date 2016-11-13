package com.example.android.gamecontroller;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Canvas;

import com.zerokol.views.JoystickView;

import co.tanvas.haptics.service.app.*;
import co.tanvas.haptics.service.adapter.*;
import co.tanvas.haptics.service.model.*;


public class HapticTest extends AppCompatActivity{

    //Tanvas Stuff
    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;
    /********************************/
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;

    private JoystickView joyStick;
    private RelativeLayout joyStickWrapper;

    private boolean joyStickCreated;


    GestureDetectorCompat gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haptictest);
        joyStickCreated = false;

        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);

        joyStickWrapper = (RelativeLayout)findViewById(R.id.joystickWrap);
        //joyStickWrapper.setOnTouchListener(this);

        createJoyStick();


        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());
        //Initialize haptics
        initHaptics();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            try {
                // Set the size and position of the haptic sprite to correspond to the view we created
                View view = findViewById(R.id.joystickView);
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                mHapticSprite.setSize(view.getWidth(), view.getHeight());
                mHapticSprite.setPosition(location[0], location[1]);
            } catch (Exception e) {
                Log.e(null, e.toString());
            }
        }
    }

    /**
     * Override: onDestroy
     * <p/>
     * <p>When the application quits or switches orientation, we will need to release all the haptic resources.</p>
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mHapticView.deactivate();
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN && !joyStickCreated) {
//            float x = event.getX();
//            float y = event.getY();
//
//            joyStick.setX(x - joyStick.getPivotX());
//            joyStick.setY(y - joyStick.getPivotY());
//            joyStick.setEnabled(true);
//            joyStick.setVisibility(View.VISIBLE);
//            joyStickCreated = true;
//            onWindowFocusChanged(true);
//            /*PROBLEM AREA*/
//            //joyStick.dispatchTouchEvent(event);
//            Log.d("drag", "drag");
//        }
//        else if (event.getAction() == MotionEvent.ACTION_UP) {
//            joyStickCreated = false;
//            joyStick.setVisibility(View.INVISIBLE);
//            joyStick.setEnabled(false);
//        }
//        return true;
//    }

    private void createJoyStick() {
        joyStick = (JoystickView) findViewById(R.id.joystickView);


        joyStick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                switch (direction) {
                    case JoystickView.FRONT:
                        directionTextView.setText("Front");
                        break;
                    case JoystickView.FRONT_RIGHT:
                        directionTextView.setText("Front Right");
                        break;
                    case JoystickView.RIGHT:
                        directionTextView.setText("Right");
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        directionTextView.setText("Right Bottom");
                        break;
                    case JoystickView.BOTTOM:
                        directionTextView.setText("Bottom");
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        directionTextView.setText("Bottom Left");
                        break;
                    case JoystickView.LEFT:
                        directionTextView.setText("Left");
                        break;
                    case JoystickView.LEFT_FRONT:
                        directionTextView.setText("Left Front");
                        break;
                    default:
                        directionTextView.setText("Center");
                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
    }

    private void initHaptics() {
        try {
            // Get the service adapter
            HapticServiceAdapter serviceAdapter = HapticApplication.getHapticServiceAdapter();

            // Create a haptic view and activate it
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();

            // Set the orientation of the haptic view
            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);
            mHapticView.setOrientation(orientation);

            // Retrieve texture data from the bitmap
            Bitmap hapticBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.radial);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);

            // Create a haptic texture with the retrieved texture data
            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);

            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);

            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);

            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            Log.e(null, e.toString());
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDoubleTap(MotionEvent event){
            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

                float x = event.getX() -50;
                float y = event.getY() -50;

                joyStick.setX(x - joyStick.getPivotX());
                joyStick.setY(y - joyStick.getPivotY());

                joyStickCreated = true;
                onWindowFocusChanged(true);
            /*PROBLEM AREA*/
                //joyStick.dispatchTouchEvent(event);
                Log.d("drag", "drag");

//            else if (event.getAction() == MotionEvent.ACTION_UP) {
//                joyStickCreated = false;
//                joyStick.setVisibility(View.INVISIBLE);
//                joyStick.setEnabled(false);
//            }
            return true;

        }
    }


}