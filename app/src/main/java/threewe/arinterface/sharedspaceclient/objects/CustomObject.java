package threewe.arinterface.sharedspaceclient.objects;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.pub.SimpleBox;
import edu.dhbw.andar.util.GraphicsUtil;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.utils.translation.ObjFormatReader;

public class CustomObject extends ARObject {

    private String objectDefinition = null;

    public CustomObject(String name, String patternName, double markerWidth, double[] markerCenter) {
        super(name, patternName, markerWidth, markerCenter);
        float   mat_ambientf[]     = {0f, 1.0f, 0f, 1.0f};
        float   mat_flashf[]       = {0f, 1.0f, 0f, 1.0f};
        float   mat_diffusef[]       = {0f, 1.0f, 0f, 1.0f};
        float   mat_flash_shinyf[] = {50.0f};

        mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf);
        mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
        mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
        mat_diffuse = GraphicsUtil.makeFloatBuffer(mat_diffusef);

    }
    public CustomObject(String name, String patternName,
                        double markerWidth, double[] markerCenter, float[] customColor) {
        super(name, patternName, markerWidth, markerCenter);
        float   mat_flash_shinyf[] = {50.0f};

        mat_ambient = GraphicsUtil.makeFloatBuffer(customColor);
        mat_flash = GraphicsUtil.makeFloatBuffer(customColor);
        mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
        mat_diffuse = GraphicsUtil.makeFloatBuffer(customColor);

    }

    public CustomObject(String name, String patternName, double markerWidth, Structure structure) {
        super(name, patternName, markerWidth, structure.position);
        float   mat_flash_shinyf[] = {50.0f};

        this.objectDefinition = structure.definition;
        ObjFormatReader formatReader = new ObjFormatReader(this.objectDefinition);
        this.box = new GenericObject(this.objectDefinition);

        mat_ambient = GraphicsUtil.makeFloatBuffer(structure.color);
        mat_flash = GraphicsUtil.makeFloatBuffer(structure.color);
        mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
        mat_diffuse = GraphicsUtil.makeFloatBuffer(structure.color);
    }

    private GenericObject box = new GenericObject();
    private FloatBuffer mat_flash;
    private FloatBuffer mat_ambient;
    private FloatBuffer mat_flash_shiny;
    private FloatBuffer mat_diffuse;

    /**
     * Everything drawn here will be drawn directly onto the marker,
     * as the corresponding translation matrix will already be applied.
     */
    @Override
    public final void draw(GL10 gl) {
        super.draw(gl);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,mat_flash);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mat_flash_shiny);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient);

        //draw cube
        gl.glColor4f(0, 1.0f, 0, 1.0f);
        gl.glTranslatef( 0.0f, 0.0f, 12.5f );

        //draw the box
//        box.draw(gl);
    }
    @Override
    public void init(GL10 gl) {

    }
}
