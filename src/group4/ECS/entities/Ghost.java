package group4.ECS.entities;

import group4.AI.Brain;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.GhostComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;


/**
 * The helper Ghost
 */
public class Ghost extends Player {
    public boolean best; // whether has reached the exit
    /**
     * @param position center point of Ghost
     * @param level    the level that the Ghost is part of
     * @param brain    NN for training purposes
     */
    public Ghost(Vector3f position, Level level, Brain brain) {
        super(position, level);
        best = false;
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.BRICK;

        //// remove player graphics
        this.remove(GraphicsComponent.class);

        //// add needed components
        this.add(new GraphicsComponent(shader, texture, this.dimension));
        this.add(new GhostComponent(brain));
    }

    public Ghost (Vector3f position, Level level, String brainPath) {
        this(position, level, new Brain(brainPath));
    }

    public static String getName() {
        return "Ghost";
    }
}
