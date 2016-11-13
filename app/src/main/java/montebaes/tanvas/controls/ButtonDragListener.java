package montebaes.tanvas.controls;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.gamecontroller.HapticObject;
import com.example.android.gamecontroller.R;

/**
 *
 */

public class ButtonDragListener implements View.OnDragListener {



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


                // Invalidates the view to force a redraw
                v.invalidate();

                // Invalidates the view to force a redraw
                v.invalidate();

                // Returns true. DragEvent.getResult() will return true.
                return true;
/*
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("drag", "ended at " + event.getX() + " " + event.getY());








                // returns true; the value is ignored.
                return true;
*/
            default:
                Log.d("drag", "wtf");

                break;
        }

        return true;
    }
}
