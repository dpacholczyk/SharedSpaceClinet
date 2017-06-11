package threewe.arinterface.sharedspaceclient.actions;

import android.util.Log;

import edu.dhbw.andar.AndARRenderer;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;

/**
 * Created by dpach on 06.06.2017.
 */

public class Highlight extends Action {

    @Override
    public void run() {
        Log.d(TAG, "highlight run");
        AndARRenderer.performAction = true;
        CustomObject.performAction = true;
//        AndARRenderer.actionName = this.getClass().getSimpleName();
//        AndARRenderer.structureId = this.getStructure().id;


    }
}
