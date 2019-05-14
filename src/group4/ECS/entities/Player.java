package group4.ECS.entities;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.*;
import group4.ECS.etc.TheEngine;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.maths.Vector3f;

// TODO: This is temporary and can be removed when a better alternative is available

public class Player extends Entity {

    /** dimension of player aka bounding box, ghost inherits in order to apply texture */
    protected Vector3f dimension = new Vector3f(1.0f, 1f, 0.0f);

    /**
     * Creates a player
     *
     * @param position center point of player
     */
    public Player(Vector3f position) {

        // vRange
        Vector3f velocityRange = new Vector3f(0.125f, 0.2f, 0.0f);
        // shader
        Shader shader = Shader.SIMPLE;
        // TODO: proper texture
        Texture texture = Texture.DEBUG;

        // add needed components
        this.add(new PositionComponent(position));
        this.add(new DimensionComponent(dimension));
        // temporary!!, player should initially not move
        this.add(new MovementComponent(new Vector3f(), velocityRange));
        this.add(new GravityComponent());
        this.add(new GraphicsComponent(shader, texture, dimension));
        this.add(new PlayerComponent());
        this.add(new ColliderComponent());
    }

}
