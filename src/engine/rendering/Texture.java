package engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

/**
 * Created by ISAKPC on 2/13/2017.
 */
public class Texture {
    //Define the variables
    private int id, width, height;

    public static Texture textSheet, characterSheet, tileSheet, npcSheet;
    //initialize the static textures
    public static void init(){
        textSheet = new Texture("textSheet.png");
        characterSheet = new Texture("characterSheet.png");
        tileSheet = new Texture("tileSheet.png");
        npcSheet = new Texture("npcSheet.png");
    }

    //Constructor
    public Texture(String filename){
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File("res/" + filename));
            width = bi.getWidth();
            height = bi.getHeight();

            int[] pixels;
            pixels = bi.getRGB(0, 0, width, height, null, 0, width);

            ByteBuffer pixelsNew = BufferUtils.createByteBuffer(width * height * 4);
            for (int i = 0; i < width * height; i++) {
                int pixel = pixels[i];
                pixelsNew.put((byte)((pixel >> 16) & 0xFF));    //RED
                pixelsNew.put((byte)((pixel >> 8) & 0xFF));     //GREEN
                pixelsNew.put((byte)(pixel & 0xFF));            //BLUE
                pixelsNew.put((byte)((pixel >> 24) & 0xFF));    //ALPHA
            }

            pixelsNew.flip();

            id = glGenTextures();

            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixelsNew);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Bind the texture for use
    public void bind(int sampler){
        glActiveTexture(GL_TEXTURE0 + sampler);
        glBindTexture(GL_TEXTURE_2D, id);
    }
}
