package threewe.arinterface.sharedspaceclient.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpach on 29.12.2016.
 */

public class Session {
    public Long id;

    public List<SessionUser> users = new ArrayList<SessionUser>();
}
