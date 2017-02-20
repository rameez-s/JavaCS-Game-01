package engine.objects;

import engine.Engine;
import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.rendering.Quad;
import engine.rendering.Shader;
import engine.rendering.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 * Created by 18iwahlqvist on 2/14/2017.
 */
public class Sprite extends Quad {
    public Vector3f position;
    public Vector3f velocity;
    public Texture texture;
    public Shader shader;

    private String texturePath, shaderPath;

    private boolean renderedBeforeSprite;

    public Sprite(){
        super();
        position = new Vector3f();
        velocity = new Vector3f();
        texturePath = "default.png";
        shaderPath = "default";
    }

    public void setTexture(String textureFile){
        texturePath = textureFile;
        renderedBeforeSprite = false;
    }

    public Sprite(float size, float z){
        super(size, z);
        position = new Vector3f();
        velocity = new Vector3f();
        texturePath = "default.png";
        shaderPath = "default";
    }

    public void update(){
        position.add(velocity);
    }

    public boolean collidesWith(Sprite s){
        boolean collidesX = Math.abs(position.x - s.position.x) < (s.size + size);
        boolean collidesY = Math.abs(position.y - s.position.y) < (s.size + size);
        return collidesX && collidesY;
    }

    public void render(){
        super.render();
        if(!renderedBeforeSprite){
            texture = new Texture(texturePath);
            shader = new Shader(shaderPath);
            renderedBeforeSprite = true;
        }
        shader.bind();
        texture.bind(1);
        shader.setUniform("sampler", 1);
        shader.setUniform("projection", currentScene.projection);
        shader.setUniform("position", position);
    }

}
