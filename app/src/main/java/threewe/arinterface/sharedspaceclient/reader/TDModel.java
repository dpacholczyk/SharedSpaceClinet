package threewe.arinterface.sharedspaceclient.reader;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;

public class TDModel {
    Vector<Float> v;
    Vector<Float> vn;
    Vector<Float> vt;
    int[] c = new int[]{0, 0, 0};
    Vector<TDModelPart> parts;
    FloatBuffer vertexBuffer;
    FloatBuffer colorBuffer;

    private boolean colorChanged = false;
    private int colorChangeCounter = 0;

    public TDModel(Vector<Float> v, Vector<Float> vn, Vector<Float> vt,
                   Vector<TDModelPart> parts) {
        super();
        this.v = v;
        this.vn = vn;
        this.vt = vt;
        this.parts = parts;
    }

    public TDModel(Vector<Float> v, Vector<Float> vn, Vector<Float> vt,
                   Vector<TDModelPart> parts, int[] color) {
        super();
        this.v = v;
        this.vn = vn;
        this.vt = vt;
        this.parts = parts;
        this.c = color;
    }

    public String toString() {
        String str = new String();
        str += "Number of parts: " + parts.size();
        str += "\nNumber of vertexes: " + v.size();
        str += "\nNumber of vns: " + vn.size();
        str += "\nNumber of vts: " + vt.size();
        str += "\n/////////////////////////\n";
        for (int i = 0; i < parts.size(); i++) {
            str += "Part " + i + '\n';
            str += parts.get(i).toString();
            str += "\n/////////////////////////";
        }
        return str;
    }

    public void draw(GL10 gl, Structure structure) {
//		gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        if (CustomObject.performAction) {
//            CustomObject.performAction = false;

            Log.d("AKCJONOWANIE", CustomObject.performedAction.getClass().getSimpleName());
            switch(CustomObject.performedAction.getClass().getSimpleName()) {
                case "Highlight":
                    if(!colorChanged && colorChangeCounter%5 == 0 && (structure != null && structure.id == CustomObject.selectedStructure.id)) {
                        this.buildColorBuffer(204, 51, 255);
                        colorChanged = true;
                    }
                    if(colorChanged && colorChangeCounter%4 == 0) {
                        this.buildColorBuffer();
                        colorChanged = false;
                    }
                    colorChangeCounter++;

                    break;
                case "ColorPicker":
                    if(structure != null && structure.id == CustomObject.selectedStructure.id) {
                        this.buildColorBuffer(Integer.parseInt((String)CustomObject.rgb.get("red")), Integer.parseInt((String)CustomObject.rgb.get("green")), Integer.parseInt((String)CustomObject.rgb.get("blue")));
                    }
                    break;
            }
            Log.d("AKCJONOWANIE", colorChanged + " | " + colorChangeCounter);

        } else {
            this.buildColorBuffer();
        }
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, colorBuffer);

        for (int i = 0; i < parts.size(); i++) {
            TDModelPart t = parts.get(i);
            Material m = t.getMaterial();

            if (m != null) {
                FloatBuffer a = m.getAmbientColorBuffer();
                FloatBuffer d = m.getDiffuseColorBuffer();
                FloatBuffer s = m.getSpecularColorBuffer();
                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, a);
                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, s);
                gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, d);
            }

            gl.glNormalPointer(GL10.GL_FLOAT, 0, t.getNormalBuffer());

            gl.glDrawElements(GL10.GL_TRIANGLES, t.getFacesCount(), GL10.GL_UNSIGNED_SHORT, t.getFaceBuffer());
        }


        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);

    }

    public void buildVertexBuffer() {
        ByteBuffer vBuf = ByteBuffer.allocateDirect(v.size() * 4);
        vBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = vBuf.asFloatBuffer();
        vertexBuffer.put(toPrimitiveArrayF(v));
        vertexBuffer.position(0);
    }

    public void buildColorBuffer() {
        ByteBuffer vBuf = ByteBuffer.allocateDirect(v.size() * 4);
        vBuf.order(ByteOrder.nativeOrder());
        float[] newColor = new float[c.length + 1];
        for (int i = 0; i < c.length; i++) {
            newColor[i] = (1.0f / 255) * c[i];
            newColor[3] = 1.0f;
        }
        colorBuffer = vBuf.asFloatBuffer();
        colorBuffer.put(newColor);
        colorBuffer.position(0);
    }

    public void buildColorBuffer(int red, int green, int blue) {
        ByteBuffer vBuf = ByteBuffer.allocateDirect(v.size() * 4);
        vBuf.order(ByteOrder.nativeOrder());
        int[] tmpC = new int[]{red, green, blue};
        float[] newColor = new float[c.length + 1];
        for (int i = 0; i < tmpC.length; i++) {
            newColor[i] = (1.0f / 255) * tmpC[i];
            newColor[3] = 1.0f;
        }
        colorBuffer = vBuf.asFloatBuffer();
        colorBuffer.put(newColor);
        colorBuffer.position(0);
    }

    private static float[] toPrimitiveArrayF(Vector<Float> vector) {
        float[] f;
        f = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            f[i] = vector.get(i);
        }
        return f;
    }
}


