package group4.ECS.entities;

import group4.AI.Brain;
import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.GhostComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Vector3f;


/**
 * The helper Ghost
 */
public class Ghost extends Player {
    public boolean best; // whether has reached the exit
    public Player master = null;
    String endTotem;

    private boolean blockMovement = false;

    /**
     * @param position center point of Ghost
     * @param level    the level that the Ghost is part of
     * @param brain    move maker of the ghost
     */
    public Ghost(Vector3f position, Level level, Brain brain) {
        super(position, level);
        best = false;
        Shader shader = Shader.SIMPLE;
        Texture texture = Texture.BRICK;

        //// remove player graphics
        this.remove(GraphicsComponent.class);

        //// add needed components
        this.add(new GraphicsComponent(shader, texture, this.dimension, false));
        this.add(new GhostComponent(brain));
    }

    public Ghost (Vector3f position, Level level, String brainPath) {
        this(position, level, new Brain(brainPath));
    }

    /**
     * Ghost constructor which also sets the master,
     * this constructor must be used when ghost is
     * spawned in-game by the player
     * @param master the player which spawned the ghost
     */
    public Ghost (Level level, Brain brain, Player master) {
        this(new Vector3f(master.getComponent(PositionComponent.class).position)
                , level, brain);
        this.master = master;
    }

    public static String getName() {
        return "Ghost";
    }

    /**
     * Whether movement is blocked
     */
    public boolean isBlocked() {
        return this.blockMovement;
    }

    /**
     * Set whether or not movement should be blocked
     * @param blocked new value for the movement block
     */
    public void setBlocked(boolean blocked) {
        this.blockMovement = blocked;
    }
}
