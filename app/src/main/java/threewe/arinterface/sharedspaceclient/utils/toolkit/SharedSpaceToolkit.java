package threewe.arinterface.sharedspaceclient.utils.toolkit;

import android.content.res.Resources;

import java.io.File;
import java.io.IOException;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.util.IO;

/**
 * Created by dpach on 06.11.2016.
 */

public class SharedSpaceToolkit extends ARToolkit {
    public SharedSpaceToolkit(Resources res, File baseFile) {
        super(res, baseFile);
    }

    

}
