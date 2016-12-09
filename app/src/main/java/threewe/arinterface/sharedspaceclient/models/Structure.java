package threewe.arinterface.sharedspaceclient.models;

/**
 * Created by dpach on 13.11.2016.
 */

public class Structure {
    public Long id;

    public String name;

    public float[] color;

    public double[] position;

    public String definition;

    public Structure() {

    }

    public Structure(Long id, String name, float[] color, double[] position, String definition) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.position = position;
        this.definition = definition;
    }

    public Structure(Long id, String name, float colorR, float colorG, float colorB, double positionX, double positionY, String def) {
        this.id = id;
        this.name = name;
        this.color = new float[]{colorR, colorG, colorB};
        this.position = new double[]{positionX, positionY};
        this.definition = def;
    }
}
