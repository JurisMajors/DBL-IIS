package group4.ECS.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import group4.ECS.etc.Families;

public class PhysicsSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    public PhysicsSystem() {}

    public PhysicsSystem(int priority) {}

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Families.physicsFamily);
    }

    public void removedFromEngine(Engine engine) {}

    public void update(float deltaTime) {}

    public boolean checkProcessing() { return false; }

    public void setProcessing(boolean processing) {}

}
