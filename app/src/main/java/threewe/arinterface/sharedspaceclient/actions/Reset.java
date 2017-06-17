package threewe.arinterface.sharedspaceclient.actions;

import android.util.Log;

import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;

/**
 * Created by dpach on 06.06.2017.
 */

public class Reset extends Action {

    @Override
    public void run() {
        Log.d(TAG, "reset run");
        CustomObject.performAction = false;
        CustomObject.performedAction = null;
        CustomObject.selectedStructure = null;
        CustomObject.rgb = null;
    }

    @Override
    public void run(Structure structure) {
        this.run();
    }
}
