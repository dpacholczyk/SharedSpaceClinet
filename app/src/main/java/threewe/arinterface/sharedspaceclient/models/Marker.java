package threewe.arinterface.sharedspaceclient.models;

import edu.dhbw.andar.ARObject;

/**
 * Created by dpach on 05.11.2016.
 */

public class Marker {
    public Long id;

    public String name;

    public String fileName = null;

    public String pattern = null;

    private String localFileName = null;

    private Structure structure = null;

    private ARObject arObject = null;

    public Marker() {

    }

    public Marker(Long id, String name, String fileName, String patter) {
        this.id = id;
        this.name = name;
        this.fileName = fileName;
        this.pattern = patter;

        this.localFileName = "pattern_" + this.fileName + "_" + this.id + ".patt";
    }

    public void setLocalFileName() {
        this.localFileName = "pattern_" + this.fileName + "_" + this.id + ".patt";
    }

    public String getLocalFileName() {
        return this.localFileName;
    }

    public void setStucture(Structure s) {
        this.structure = s;
    }

    public Structure getStructure() {
        return this.structure;
    }

    public void setARObject(ARObject arObject) {
        this.arObject = arObject;
    }

    public ARObject getARObject() {
        return arObject;
    }
}
