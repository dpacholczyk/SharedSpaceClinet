package threewe.arinterface.sharedspaceclient.utils.translation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dpach on 08.12.2016.
 * testing x3
 */

public class ObjFormatReader {
    private String data;
    private List<Float> vectors;
    private List<Float> normals;

    public ObjFormatReader(String data) {
        this.vectors = new ArrayList<Float>();
        this.normals = new ArrayList<Float>();

        String[] lines = data.split("\n");
        for(String line : lines) {
            if(line.substring(0, 2).equals("vn")) { // normals
                String[] elements = line.split(" ");
                for(int i = 1; i < elements.length; i++) {
                    if((Float)Float.parseFloat(elements[i]) != null) {
                        this.normals.add(Float.parseFloat(elements[i]));
                    }
                }
            } else if(line.substring(0, 2).equals("v ")) {  // wektory
                String[] elements = line.split(" ");
                for(int i = 1; i < elements.length; i++) {
                    if((Float)Float.parseFloat(elements[i]) != null) {
                        this.vectors.add(Float.parseFloat(elements[i]));
                    }
                }
            }
        }
    }

    public List<Float> getVectors() {
        return this.vectors;
    }

    public Float getVector(int position) {
        return this.vectors.get(position);
    }

    public List<Float> getNormals() {
        return this.normals;
    }

    public Float getNormal(int position) {
        return this.normals.get(position);
    }
}
