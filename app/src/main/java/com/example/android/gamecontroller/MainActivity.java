package com.example.android.gamecontroller;

import android.content.ClipData;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Point;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;



import montebaes.tanvas.controls.ButtonDragListener;

public class MainActivity extends AppCompatActivity {
    /* MODE */
    private static boolean MODE = false;
    /* Log Declaration */
    final String LOG_MODE = "MODE CHANGE : ";
    GestureDetectorCompat gestureDetector;


    View.OnLongClickListener longListen = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("","");

            DragShadow dragShadow = new DragShadow(view);

            view.startDrag(data, dragShadow, view, 0);
            return false;
        }
    };
    /* Content */
    private TextView modeType;
    private ImageButton redButton, greenButton;
    private HapticObject redButtonHaptic, greenButtonHaptic;
    private HapticObject joystickHaptic;
    private TextView angleTextView;
    private TextView powerTextView;
    private TextView directionTextView;
    private DynamicJoystick joyStick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        initJoystickField();
        initButtonField();
        initDragListener();
        initSwitch();



    }

    private void initSwitch(){
        ((Switch)findViewById(R.id.hapticSwitch)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                joystickHaptic.toggleView(isChecked);
                greenButtonHaptic.toggleView(!isChecked);
                redButtonHaptic.toggleView(!isChecked);
            }
        });
    }
    
    private void initJoystickField(){
        angleTextView = (TextView) findViewById(R.id.angleTextView);
        powerTextView = (TextView) findViewById(R.id.powerTextView);
        directionTextView = (TextView) findViewById(R.id.directionTextView);

        createJoyStick();


        gestureDetector = new GestureDetectorCompat(this, new MyGestureListener());

        joystickHaptic = new HapticObject(this, R.drawable.radial);
        joystickHaptic.toggleView(false);
    }
    
    private void initButtonField(){

        modeType = (TextView)findViewById(R.id.mode_type);
        modeType.setText(R.string.mode_false);

        greenButton = (ImageButton) findViewById(R.id.button_green);

        greenButton.setImageResource(R.drawable.greena);

        redButton = (ImageButton) findViewById(R.id.button_red);

        findViewById(R.id.activity_main).setOnDragListener(new ButtonDragListener());

        redButtonHaptic = new HapticObject(this, R.drawable.noise_texture );
        greenButtonHaptic = new HapticObject(this, R.drawable.noise_texture);
    }

    private void initDragListener(){
        findViewById(R.id.buttonField).setOnDragListener(new ButtonDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();
                switch(action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d("drag", "started at " + event.getX() + " " + event.getY());

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();
                        Log.d("drag", "started at " + event.getX() + " " + event.getY());
                        // returns true to indicate that the View can accept the dragged data.
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("drag", "entered");

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:

                        // Ignore the event
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("drag", "exited");

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return true;

                    case DragEvent.ACTION_DROP:
                        Log.d("drag", "drop" + event.getX() + " " + event.getY());

                        //RelativeLayout target = (RelativeLayout) v;
                        ImageButton dragged = (ImageButton) event.getLocalState();
                        //dragged.setImageResource(R.drawable.black);
                        dragged.setX(event.getX());
                        dragged.setY(event.getY());
                        //target.findViewById(dragged.getId());
                        onWindowFocusChanged(true, dragged.getId());
                        // Invalidates the view to force a redraw
                        v.invalidate();
                        // Returns true. DragEvent.getResult() will return true.
                        return true;
                    default:
                        Log.d("drag", "wtf");
                        break;
                }
                return true;
            }
        });

    }

    public void toggleButtonDrag(View v){
        //Drag and Drop Button
        if(v.getId() == R.id.drag_drop_button) {
            if (MODE) {
                changeMODE(false);
                modeType.setText(R.string.mode_false);
                unSetAllLongClickListener();

            } else {
                changeMODE(true);
                modeType.setText(R.string.mode_true);

                setAllLongClickListener();
            }
        }
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

    public void changeMODE(boolean MODE){
        this.MODE = MODE;
        Log.v(LOG_MODE, String.valueOf(MODE));
    }


    public void onWindowFocusChanged(boolean hasFocus, int containerID) {
        super.onWindowFocusChanged(hasFocus);
        if(containerID == R.id.button_green)
            greenButtonHaptic.onWindowFocusChanged(this, hasFocus, containerID);
        else if(containerID == R.id.button_red)
            redButtonHaptic.onWindowFocusChanged(this,hasFocus, containerID);
        
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        joystickHaptic.onWindowFocusChanged(this, true, R.id.joystickView);
        greenButtonHaptic.onWindowFocusChanged(this, hasFocus, R.id.button_green);
        redButtonHaptic.onWindowFocusChanged(this,hasFocus, R.id.button_red);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        greenButtonHaptic.onDestroy();
        redButtonHaptic.onDestroy();
        joystickHaptic.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("Event Action", event.toString());
        gestureDetector.onTouchEvent(event);
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
    
    private class DragShadow extends View.DragShadowBuilder{

        public ImageButton var;

        public DragShadow(View view){
            super(view);
            var = (ImageButton)view;
        }

        @Override
        public void onDrawShadow(Canvas canvas){
            //Canvas is the area of the shadow
            var.draw(canvas);
        }

        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint){
            View v = getView();

            int height = v.getHeight();
            int width = v.getWidth();


            shadowSize.set(width, height);

            //Drag exactly in the center
            shadowTouchPoint.set(width/2, height/2);
        }



    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDoubleTap(MotionEvent event){
            Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());

            float x = event.getX();
            float y = event.getY();

            View joystickField = findViewById(R.id.joystickField);


                joyStick.setX(x - 300);
                joyStick.setY(y - 300);


                onWindowFocusChanged(true);
            /*PROBLEM AREA*/
                //joyStick.dispatchTouchEvent(event);
                Log.d("drag", "drag");


            return true;

        }
    }


}

