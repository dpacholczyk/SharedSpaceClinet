package threewe.arinterface.sharedspaceclient.actions;

import java.util.HashMap;

import threewe.arinterface.sharedspaceclient.models.Structure;

/**
 * Created by dpach on 06.06.2017.
 */

public class ActionPicker {
    public HashMap<String, Object> rgb = null;

    public void performAction(String actionName, Structure structure) {
        try {
            Class actionClass = Class.forName(actionName);
            Action action = (Action)actionClass.newInstance();

            if(this.rgb != null) {
                action.setColor(this.rgb);
            }

            if(structure == null) {
                action.run();
            } else {
                action.run(structure);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
