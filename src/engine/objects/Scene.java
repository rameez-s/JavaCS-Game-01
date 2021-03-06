package engine.objects;

import engine.math.Matrix4f;
import engine.math.Vector3f;
import engine.rendering.Quad;
import engine.rendering.Texture;

import java.util.ArrayList;

/**
 * Created by Isak Wahlqvist
 */

public abstract class Scene {
    public boolean instructionsShown = false;
    public ArrayList<Sprite> elements = new ArrayList<>();
    public ArrayList<Sprite> npc = new ArrayList<>();
    public ArrayList<Sprite> players = new ArrayList<>();
    public ArrayList<Projectile> projectiles = new ArrayList<>();

    public boolean hasMap=true;

    public boolean optimize = true;
    Map map;

    public Matrix4f projection = new Matrix4f().orthographic(-512, 512, -512, 512, 10, -10);

    public Scene(){
        projection = new Matrix4f().orthographic(-512, 512, -512, 512, 10, -10);
    }

    public void setMap(String fileName){
        map = new Map(fileName);
    }

    public void add(Quad element, int array){
        switch (array){
            case 0:
                elements.add((Sprite) element);
                break;
            case 1:
                npc.add((Sprite) element);
                break;
            case 2:
                projectiles.add((Projectile) element);
                break;
            case 3:
                players.add((Sprite) element);
                break;
            default:
                throw new UnknownError();
        }
        element.currentScene = this;
    }

    public void set(Quad element, int array, int pos){
        switch (array){
            case 0:
                if(elements.size() > pos) {
                    elements.set(pos, (Sprite) element);
                }else{
                    elements.add((Sprite) element);
                }
                break;
            case 1:
                if(npc.size() > pos) {
                    npc.set(pos, (Sprite) element);
                }else{
                    npc.add((Sprite) element);
                }
                break;
            case 2:
                if(projectiles.size() > pos) {
                    projectiles.set(pos, (Projectile) element);
                }else{
                    projectiles.add((Projectile) element);
                }
                break;
            case 3:
                if(players.size() > pos) {
                    players.set(pos, (Sprite) element);
                }else{
                    players.add((Sprite) element);
                }
                break;
            default:
                throw new UnknownError();
        }
        element.currentScene = this;
    }

    public boolean remove(Quad element, int array){
        switch (array){
            case 0:
                return elements.remove(element);
            case 1:
                return npc.remove(element);
            case 2:
                return projectiles.remove(element);
        }
        return elements.remove(element);
    }

    public void genMap(){
        Vector3f position = new Vector3f(-map.mapData.length/2 * 128, -map.mapData[0].length/2 * 128, 0f);
        System.out.println(map.mapData.length + "\t" + map.mapData[0].length);
        for(int x = 0; x < map.mapData.length; x++){
            for(int y = 0; y < map.mapData[0].length; y++){
                position.add(new Vector3f(0, (128  * 1.7777777777777777777777777777778f), 0));
                Sprite s = new Sprite(128, 0f);
                s.texture = Texture.tileSheet;
                s.position.set(position.x, position.y, position.z);
                switch (map.mapData[x][y]) {
                    case 0:
                        s.textureCoords.set(0.125f, 0f);
                        break;
                    case 1:
                        s.textureCoords.set(0.125f, 0.125f);
                        map.collidablePixels.add(s);
                        break;
                    case 2:
                        s.textureCoords.set(0, 0.125f);
                        break;
                    case 3:
                        s.textureCoords.set(0.25f, 0.125f);
                        break;
                    case 4:
                        s.textureCoords.set(0, 0);
                        break;
                }
                add(s, 0);
                s.update();
                s.render();
            }
            position.add(new Vector3f(128, -(map.mapData[0].length) * (128 * 1.7777777777777777777777777777778f), 0));
        }
    }

    public void update(){
        for (int i = elements.size() - 1; i >= 0; i--) {
            if(players.size() > 0 && optimize) {
                if (elements.get(i).position.distance(players.get(0).position) < 1000) {
                    elements.get(i).update(true);
                }
            }else{
                elements.get(i).update(true);
            }
        }
        for (int i = npc.size() - 1; i >= 0; i--) {
            npc.get(i).update();
        }
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).update();
        }
        for (int i = players.size() - 1; i >= 0; i--) {
            players.get(i).update();
        }
    }

    public void render(){
        for (int i = elements.size() - 1; i >= 0; i--) {
            if(players.size() > 0 && optimize) {
                if (elements.get(i).position.distance(players.get(0).position) < 1000) {
                    if (elements.get(i).position.distance(players.get(0).position) < 1000) {
                        elements.get(i).render();
                    }
                }
            }else{
                elements.get(i).render();
            }
        }
        for (int i = npc.size() - 1; i >= 0; i--) {
            npc.get(i).render();
        }
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            projectiles.get(i).render();
        }
        for (int i = players.size() - 1; i >= 0; i--) {
            players.get(i).render();
        }
    }
}
