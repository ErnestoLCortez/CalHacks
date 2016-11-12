package montebaes.tanvas.controls;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Toast;

/**
 *
 */

public class ButtonDragListener implements View.OnDragListener {

    @Override
    public boolean onDrag(View v, DragEvent event) {

        final int action = event.getAction();

        switch(action) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d("drag", "started");

                // Invalidate the view to force a redraw in the new tint
                v.invalidate();

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
                Log.d("drag", "drop");

                // Invalidates the view to force a redraw
                v.invalidate();

                // Returns true. DragEvent.getResult() will return true.
                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.d("drag", "ended");

                // Invalidates the view to force a redraw
                v.invalidate();

                v.setX(event.getX());
                v.setY(event.getY());

                // returns true; the value is ignored.
                return true;

            default:
                Log.d("drag", "wtf");

                break;
        }

        return false;
    }
}
