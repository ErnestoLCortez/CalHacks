package com.example.android.gamecontroller;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



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
    HapticObject redButtonHaptic, greenButtonHaptic;

    ImageView testMoveImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dragDropButton = (Button) findViewById(R.id.drag_drop_button);
        dragDropButton.setOnClickListener(this);


        modeType = (TextView) findViewById(R.id.mode_type);
        modeType.setOnClickListener(this);
        modeType.setText(R.string.mode_false);

        greenButton = (ImageButton) findViewById(R.id.button_green);
        greenButton.setOnClickListener(this);
        greenButton.setImageResource(R.drawable.greena);

        HapticObject greenHaptic = new HapticObject(this, R.drawable.noise_texture);

        redButton = (ImageButton) findViewById(R.id.button_red);
        redButton.setOnClickListener(this);
        redButton.setImageResource(R.drawable.redb);


        HapticObject redHaptic = new HapticObject(this, R.drawable.noise_texture);

        findViewById(R.id.activity_main).setOnDragListener(new ButtonDragListener());

        redButtonHaptic = new HapticObject(this, R.drawable.noise_texture );
        greenButtonHaptic = new HapticObject(this, R.drawable.noise_texture);

        findViewById(R.id.activity_main).setOnDragListener(new ButtonDragListener(){


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


    public void onWindowFocusChanged(boolean hasFocus, int containerID) {
        super.onWindowFocusChanged(hasFocus);
        if(containerID == R.id.button_green)
            greenButtonHaptic.onWindowFocusChanged(this, hasFocus, containerID);
        else
            redButtonHaptic.onWindowFocusChanged(this,hasFocus, containerID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        greenButtonHaptic.onDestroy();
        redButtonHaptic.onDestroy();
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

