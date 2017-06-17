package threewe.arinterface.sharedspaceclient.actions;

import java.util.HashMap;

import threewe.arinterface.sharedspaceclient.config.ActivityType;
import threewe.arinterface.sharedspaceclient.models.Structure;

/**
 * Created by dpach on 03.06.2017.
 */

public abstract class Action {
    public static final String ACTIONS_PACKAGE = "threewe.arinterface.sharedspaceclient.actions.";

    public static final String TAG = "PERFORM_ACTION";

    private HashMap<String, Object> color = null;

    private ActivityType activityType;

    private Structure structure;

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public HashMap<String, Object> getColor() {
        return this.color;
    }

    public void setColor(HashMap<String, Object> color) {
        this.color = color;
    }

    public abstract void run();

    public abstract void run(Structure structure);
}
