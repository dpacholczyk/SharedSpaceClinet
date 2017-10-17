package threewe.arinterface.sharedspaceclient.actions;

import android.util.Log;

import threewe.arinterface.sharedspaceclient.models.Structure;

/**
 * Created by dpach on 06.06.2017.
 */

public class Highlight extends Action {

    @Override
    public void run() {
        Log.d(TAG, "highlight run");
//        CustomObject.performAction = true;
//        CustomObject.performedAction = this;

    }

    @Override
    public void run(Structure structure) {
        this.run();
//        CustomObject.selectedStructure = structure;
    }
}
