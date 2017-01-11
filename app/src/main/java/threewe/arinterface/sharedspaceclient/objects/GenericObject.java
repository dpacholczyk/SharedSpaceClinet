package threewe.arinterface.sharedspaceclient.objects;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.util.GraphicsUtil;
import threewe.arinterface.sharedspaceclient.utils.translation.ObjFormatReader;

public class GenericObject {
    private FloatBuffer box;
    private FloatBuffer normals;

    public GenericObject() {
        float boxf[] =  {
                // FRONT
                -25.0f, -25.0f,  25.0f,
                25.0f, -25.0f,  25.0f,
                -25.0f,  25.0f,  25.0f,
                25.0f,  25.0f,  25.0f,
                // BACK
                -25.0f, -25.0f, -25.0f,
                -25.0f,  25.0f, -25.0f,
                25.0f, -25.0f, -25.0f,
                25.0f,  25.0f, -25.0f,
                // LEFT
                -25.0f, -25.0f,  25.0f,
                -25.0f,  25.0f,  25.0f,
                -25.0f, -25.0f, -25.0f,
                -25.0f,  25.0f, -25.0f,
                // RIGHT
                25.0f, -25.0f, -25.0f,
                25.0f,  25.0f, -25.0f,
                25.0f, -25.0f,  25.0f,
                25.0f,  25.0f,  25.0f,
                // TOP
                -25.0f,  25.0f,  25.0f,
                25.0f,  25.0f,  25.0f,
                -25.0f,  25.0f, -25.0f,
                25.0f,  25.0f, -25.0f,
                // BOTTOM
                -25.0f, -25.0f,  25.0f,
                -25.0f, -25.0f, -25.0f,
                25.0f, -25.0f,  25.0f,
                25.0f, -25.0f, -25.0f,
        };
        float normalsf[] =  {
                // FRONT
                0.0f, 0.0f,  1.0f,
                0.0f, 0.0f,  1.0f,
                0.0f, 0.0f,  1.0f,
                0.0f, 0.0f,  1.0f,
                // BACK
                0.0f, 0.0f,  -1.0f,
                0.0f, 0.0f,  -1.0f,
                0.0f, 0.0f,  -1.0f,
                0.0f, 0.0f,  -1.0f,
                // LEFT
                -1.0f, 0.0f,  0.0f,
                -1.0f, 0.0f,  0.0f,
                -1.0f, 0.0f,  0.0f,
                -1.0f, 0.0f,  0.0f,
                // RIGHT
                1.0f, 0.0f,  0.0f,
                1.0f, 0.0f,  0.0f,
                1.0f, 0.0f,  0.0f,
                1.0f, 0.0f,  0.0f,
                // TOP
                0.0f, 1.0f,  0.0f,
                0.0f, 1.0f,  0.0f,
                0.0f, 1.0f,  0.0f,
                0.0f, 1.0f,  0.0f,
                // BOTTOM
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
                0.0f, -1.0f,  0.0f,
        };

        box = GraphicsUtil.makeFloatBuffer(boxf);
        normals = GraphicsUtil.makeFloatBuffer(normalsf);
    }

    public GenericObject(String data) {

        ObjFormatReader reader = new ObjFormatReader(data);
        float[] vectors = new float[reader.getVectors().size() * 3];
        float[] normalsInfo = new float[reader.getVectors().size() * 3];

        int v = 0;
        int n = 0;
        List<Float[]> points = reader.getPointsScheme();
        for(Float[] set : points) {
            for(int i = 0; i < set.length; i++) {
                if(set[i] != null) {
                    vectors[v] = set[i];
                    v++;
                }
            }
        }

        List<Float[]> nPoints = reader.getNormalsScheme();
        for(Float[] normalsSet : nPoints) {
            for(int i = 0; i < normalsSet.length; i++) {
                if(normalsSet[i] != null) {
                    normalsInfo[n] = normalsSet[i];
                    n++;
                }
            }
        }

        box = GraphicsUtil.makeFloatBuffer(vectors);
        normals = GraphicsUtil.makeFloatBuffer(normalsInfo);
        int t = 1;
    }


    public final void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, box);
        gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
//        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 16, GL10.GL_UNSIGNED_BYTE, box);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }
}
