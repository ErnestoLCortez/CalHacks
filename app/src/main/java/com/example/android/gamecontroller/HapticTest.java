package com.example.android.gamecontroller;


import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class HapticTest extends AppCompatActivity{

    //Tanvas Stuff
    HapticObject joysticHaptic;
    /********************************/
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;

    private DynamicJoystick joyStick;
    private RelativeLayout joyStickWrapper;

    GestureDetectorCompat gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haptictest);

        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);

        joyStickWrapper = (RelativeLayout)findViewById(R.id.joystickWrap);
        //joyStickWrapper.setOnTouchListener(this);

        createJoyStick();


        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());
        //Initialize haptics
        joysticHaptic = new HapticObject(this, R.drawable.radial);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        joysticHaptic.onWindowFocusChanged(this, true, R.id.joystickView);

    }


    /**
     * Override: onDestroy
     * <p/>
     * <p>When the application quits or switches orientation, we will need to release all the haptic resources.</p>
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        joysticHaptic.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private void createJoyStick() {
        joyStick = (DynamicJoystick) findViewById(R.id.joystickView);


        joyStick.setOnJoystickMoveListener(new DynamicJoystick.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                angleTextView.setText(" " + String.valueOf(angle) + "°");
                powerTextView.setText(" " + String.valueOf(power) + "%");
                switch (direction) {
                    case DynamicJoystick.FRONT:
                        directionTextView.setText("Front");
                        break;
                    case DynamicJoystick.FRONT_RIGHT:
                        directionTextView.setText("Front Right");
                        break;
                    case DynamicJoystick.RIGHT:
                        directionTextView.setText("Right");
                        break;
                    case DynamicJoystick.RIGHT_BOTTOM:
                        directionTextView.setText("Right Bottom");
                        break;
                    case DynamicJoystick.BOTTOM:
                        directionTextView.setText("Bottom");
                        break;
                    case DynamicJoystick.BOTTOM_LEFT:
                        directionTextView.setText("Bottom Left");
                        break;
                    case DynamicJoystick.LEFT:
                        directionTextView.setText("Left");
                        break;
                    case DynamicJoystick.LEFT_FRONT:
                        directionTextView.setText("Left Front");
                        break;
                    default:
                        directionTextView.setText("Center");
                }
            }
        }, DynamicJoystick.DEFAULT_LOOP_INTERVAL);
    }



    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDoubleTap(MotionEvent event){
            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

                float x = event.getX();
                float y = event.getY();

                joyStick.setX(x - joyStick.getPivotX());
                joyStick.setY(y - joyStick.getPivotY());


                onWindowFocusChanged(true);
            /*PROBLEM AREA*/
                //joyStick.dispatchTouchEvent(event);
                Log.d("drag", "drag");


            return true;

        }
    }


}