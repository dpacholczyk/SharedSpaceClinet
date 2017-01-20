package threewe.arinterface.sharedspaceclient.utils.translation;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dpach on 08.12.2016.
 * testing x3
 */

public class ObjFormatReader {
    private String data;
    private List<Float> vectors;
    private List<Float> normals;
    private List<Float[]> scheme;
    private List<Float[]> normalsScheme;

    public ObjFormatReader(String data) {
        this.vectors = new ArrayList<Float>();
        this.normals = new ArrayList<Float>();
        this.scheme = new ArrayList<Float[]>();
        this.normalsScheme = new ArrayList<Float[]>();

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
            } else if(line.substring(0, 1).equals("f")) { // schemat
                String[] elements = line.split(" ");
                Float[] points = new Float[elements.length-1];
                Float[] normalsVectors = new Float[elements.length-1];

                for(int i = 1; i < elements.length; i++) {
                    String[] info = elements[i].split("//");
//                    points[i-1] = Float.parseFloat(info[0]);
                    Integer point = Integer.parseInt(info[0].replace("\r", ""));
                    Integer nPoint = Integer.parseInt(info[1].replace("\r", ""));

                    Float x, nx = null;
                    Float y, ny = null;
                    Float z, nz = null;
                    int first = 0;
                    int nFirst = 0;
                    points = new Float[elements.length-1];
                    normalsVectors = new Float[elements.length-1];

                    if(point == 1) {
                        x = this.vectors.get(0);
                        y = this.vectors.get(1);
                        z = this.vectors.get(2);
                    } else {
                        first = (point - 1) * 3;
                        x = this.vectors.get(first);
                        y = this.vectors.get(first+1);
                        z = this.vectors.get(first+2);
                        int zz = 1;
                    }

                    if(nPoint == 1) {
                        nx = this.normals.get(0);
                        ny = this.normals.get(1);
                        nz = this.normals.get(2);
                    } else {
                        nFirst = (nPoint - 1) * 3;
                        nx = this.normals.get(nFirst);
                        ny = this.normals.get(nFirst+1);
                        nz = this.normals.get(nFirst+2);
                    }

                    points[0] = x;
                    points[1] = y;
                    points[2] = z;

                    normalsVectors[0] = nx;
                    normalsVectors[1] = ny;
                    normalsVectors[2] = nz;

                    this.scheme.add(points);
                    this.normalsScheme.add(normalsVectors);
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

    public List<Float[]> getPointsScheme() {
        return this.scheme;
    }

    public List<Float[]> getNormalsScheme() {
        return this.normalsScheme;
    }
}
