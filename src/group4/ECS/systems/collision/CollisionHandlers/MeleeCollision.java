package group4.ECS.systems.collision.CollisionHandlers;

import com.badlogic.ashley.core.Entity;
import group4.ECS.components.physics.CollisionComponent;
import group4.ECS.components.stats.DamageComponent;
import group4.ECS.components.stats.HealthComponent;
import group4.ECS.components.stats.MovementComponent;
import group4.ECS.entities.damage.DamageArea;
import group4.ECS.entities.hazards.Spikes;
import group4.ECS.etc.EntityConst;
import group4.ECS.etc.Mappers;
import group4.ECS.systems.collision.CollisionData;

import java.util.Set;

public class MeleeCollision extends AbstractCollisionHandler<Entity> {

    /** Singleton **/
    private static AbstractCollisionHandler me = new MeleeCollision();

    @Override
    public void collision(Entity e, CollisionComponent cc) {
        Set<CollisionData> others = cc.collisions;

        for (CollisionData cd : others) {
            Entity other = cd.entity;

            // if entity is excluded from damage, skip
            if (Mappers.damageMapper.get(e).excluded.contains(other.getClass())) {
                continue;
            }

            // compute damage and knockback
            if (Mappers.healthMapper.get(other) != null) {
                handleDmg(e, other);
                handleKnockback(e, other);
            }
        }

        // clear collisions
        others.clear();
    }

    private void handleDmg(Entity entity, Entity other) {
        HealthComponent hc = other.getComponent(HealthComponent.class);
        DamageComponent dmg = entity.getComponent(DamageComponent.class);

        // if other does not have health
        if (hc == null) {
            return;
        }

        // if entity immune to dmg, skip
//        System.out.println(hc.state);
        if (hc.state.contains(EntityConst.EntityState.IMMUNE)) {
            return;
        }

        // deal Dmg
        hc.health -= dmg.damage;
//        System.out.println(hc.health);

        // make immune for one tick (see LastSystem)
        hc.state.add(EntityConst.EntityState.IMMUNE);
//        System.out.println(hc.state);
    }

    private void handleKnockback(Entity entity, Entity other) {
        MovementComponent mc = Mappers.movementMapper.get(other);
        HealthComponent hc = Mappers.healthMapper.get(other);

        // if not a moving object, don't apply knockback
        if (mc == null) {
            return;
        }

        // TODO: should be for all hazardous entities
        // behaviour of hazardous entities
        if (entity instanceof Spikes) {

//            System.out.println(hc.state);
            if (hc.state.contains(EntityConst.EntityState.KNOCKED)) {
                return;
            }

            // min knockback velocity
            float minKnockBack = 0.15f;

            // if incoming velocity too low, set outgoing vector to minimum knockback
            float boost = 1.0f;
            if (mc.velocity.length() < minKnockBack) {
                boost = minKnockBack / mc.velocity.length();
            }

//            // TODO: this works only for top and bottom spikes
//            if (mc.velocity.y < 0) {
//                mc.velocity.y *= -1;
//            }
//
//            if (mc.velocity.x < 0) {
//                mc.velocity.x *= -1;
//            }

//            System.out.println(mc.velocity);
            mc.velocity = mc.velocity.scale(-boost);
//            System.out.println(mc.velocity);
            hc.state.add(EntityConst.EntityState.KNOCKED);
//            System.out.println(hc.state);
        }

        if (entity instanceof DamageArea) {
            // TODO: handle knockback differently
        }
    }

    /**
     * @return singleton instance of this collision handler
     */
    public static AbstractCollisionHandler getInstance() {
        return me;
    }

}
