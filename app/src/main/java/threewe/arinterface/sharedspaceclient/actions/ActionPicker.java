package threewe.arinterface.sharedspaceclient.actions;

import threewe.arinterface.sharedspaceclient.models.Structure;

/**
 * Created by dpach on 06.06.2017.
 */

public class ActionPicker {
    public static void performAction(String actionName, Structure structure) {
        try {
            Class actionClass = Class.forName(Action.ACTIONS_PACKAGE + actionName);
            Action action = (Action)actionClass.newInstance();
            action.run();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
