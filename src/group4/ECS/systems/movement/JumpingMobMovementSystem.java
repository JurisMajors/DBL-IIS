package group4.ECS.systems.movement;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.etc.Families;
import group4.ECS.etc.Mappers;
import group4.maths.Vector3f;

public class JumpingMobMovementSystem extends MobMovementSystem {

    public JumpingMobMovementSystem() {
        super();
    }
    public JumpingMobMovementSystem(Family family) {
        super();
    }

    @Override
    protected boolean canMoveLeft(Vector3f velocity) {
        super.canMoveLeft(velocity);
        // jumping mobs can only move left when in air
        return !(velocity.y <= 1e-3 && velocity.y >= -1e-3);
    }

    @Override
    protected boolean canMoveRight(Vector3f velocity) {
        super.canMoveRight(velocity);
        // jumping mobs can only move right when in air
        return !(velocity.y <= 1e-3 && velocity.y >= -1e-3);
    }

    @Override
    protected void move(Entity e, Vector3f targetPosition, float deltaTime) {
        super.move(e, targetPosition, deltaTime);
        PositionComponent pc = Mappers.positionMapper.get(e);
        MovementComponent mc = Mappers.movementMapper.get(e);
        // set velocity in the direction towards the player
        if (pc.position.y < targetPosition.y && canJump(mc.velocity)) {
            jump(mc);
        } else if (pc.position.x < targetPosition.x && !canMoveRight(mc.velocity) && canJump(mc.velocity)) {
            jump(mc);
            moveRight(mc);
        } else if (pc.position.x < targetPosition.x && canMoveRight(mc.velocity)) {
            moveRight(mc);
        } else if (pc.position.x > targetPosition.x && !canMoveLeft(mc.velocity) && canJump(mc.velocity)) {
            jump(mc);
            moveLeft(mc);
        } else if (pc.position.x > targetPosition.x && canMoveLeft(mc.velocity)) {
            moveLeft(mc);
        } else {
            mc.velocity.x = 0;
        }
    }
}
