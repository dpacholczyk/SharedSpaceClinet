package threewe.arinterface.sharedspaceclient.models;

/**
 * Created by dpach on 13.11.2016.
 */

public class Structure {
    public Long id;

    public String name;

    public int[] color;

    public double[] position;

    public String definition;

    public Structure() {

    }

    public Structure(Long id, String name, int[] color, double[] position, String definition) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.position = position;
        this.definition = definition;
    }

    public Structure(Long id, String name, int colorR, int colorG, int colorB, double positionX, double positionY, String def) {
        this.id = id;
        this.name = name;
        this.color = new int[]{colorR, colorG, colorB};
        this.position = new double[]{positionX, positionY};
        this.definition = def;
    }

    public float[] getColorf() {
        float[] tmp = new float[color.length];
        tmp[0] = 1 / (float)color[0];
        tmp[1] = 1 / (float)color[1];
        tmp[2] = 1 / (float)color[2];

        return tmp;
    }

    public float[] getColorfTest() {
        float[] tmp = new float[color.length];
        tmp[0] = 1 / (float)0;
        tmp[1] = 1 / (float)0;
        tmp[2] = 1 / (float)0;

        return tmp;
    }
}
