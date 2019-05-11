package group4.ECS.etc;

/**
 * constant ID's for item, trap, enemy, ... related game mechanics
 *
 * not final, we might in the end not use enums....
 */
public class EntityConst {

    // TODO: add items
    public enum ItemID {
        UNDEFINED_ITEM,
        HEALTH_POTION,
        PISTOL
    }

    // TODO: add effects
    public enum EffectID  {
        UNDEFINED_EFFECT,
        SMALL_HEAL,
        MEDIUM_HEAL,
        LARGE_HEAL,
        SMALL_POISON
    }

    // TODO: add bullet types
    public enum BulletType {
        UNDEFINED_TYPE,
        PISTOL,
        MACHINEGUN,
    }

}
