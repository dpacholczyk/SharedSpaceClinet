package threewe.arinterface.sharedspaceclient.actions;

import android.util.Log;

import java.util.HashMap;

import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;

/**
 * Created by dpach on 06.06.2017.
 */

public class ColorPicker extends Action {

    @Override
    public void run() {
        Log.d(TAG, "color picker run");
        CustomObject.performAction = true;
        CustomObject.performedAction = this;

    }

    @Override
    public void run(Structure structure) {
        this.run();
        CustomObject.selectedStructure = structure;
        CustomObject.rgb = this.getColor();
    }
}
