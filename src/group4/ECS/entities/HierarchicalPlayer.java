package group4.ECS.entities;

import group4.ECS.components.GraphicsComponent;
import group4.ECS.components.identities.AnimationComponent;
import group4.ECS.components.physics.DimensionComponent;
import group4.ECS.components.physics.PositionComponent;
import group4.ECS.etc.EntityState;
import group4.ECS.systems.animation.AnimationSet;
import group4.ECS.systems.animation.DelayedAnimationSet;
import group4.ECS.systems.animation.IKEndEffector;
import group4.ECS.systems.animation.SplineAnimation;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.maths.Matrix4f;
import group4.maths.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;


public class HierarchicalPlayer extends Player implements GraphicsHierarchy {

    /**
     * dimension of player aka bounding box, ghost inherits in order to apply texture
     */
    protected Vector3f dimension = new Vector3f(1.0f, 2.0f, 0.0f);


    /**
     * Hierarchy of graphics components
     */
    public List<BodyPart> hierarchy = new ArrayList<>();
    public Map<String, IKEndEffector> IKHandles = new HashMap<>();

    /**
     * Directly access the bodyparts in the hierarchy
     */
    protected BodyPart torso;
    protected BodyPart rightLegUpper;
    protected BodyPart rightLegLower;
    protected BodyPart leftLegUpper;
    protected BodyPart leftLegLower;
    protected BodyPart rightArmUpper;
    protected BodyPart rightArmLower;
    protected BodyPart leftArmUpper;
    protected BodyPart leftArmLower;


    /**
     * Definition of body part dimensions
     */
    public final Vector3f upperLegDimension = new Vector3f(0.15f, 0.5f, 0.0f);
    public final Vector3f lowerLegDimension = new Vector3f(0.12f, 0.4f, 0.0f);
    public final Vector3f upperArmDimension = new Vector3f(0.1f, 0.5f, 0.0f);
    public final Vector3f lowerArmDimension = new Vector3f(0.08f, 0.4f, 0.0f);
    public final Vector3f TorsoDimension = new Vector3f(0.4f, 0.8f, 0.0f);
    public final Vector3f shoulderOffset = new Vector3f(0.0f, 0.6f, 0.0f);
    public final Vector3f hipOffset = new Vector3f(this.dimension.x / 2, 0.8f, 0.0f);

    /**
     * The position of hip of the player (a.k.a. the bottom center position of the player)
     */
    Vector3f hipPosition = new Vector3f(hipOffset);

    /**
     * Creates a player
     *
     * @param position center point of player
     * @param level    the level that the player is part of
     */
    public HierarchicalPlayer(Vector3f position, Level level) {
        super(position, level);

        // TODO: For testing purposes set default anim here! Works until movementsystem is updated.
        this.state = EntityState.PLAYER_WALKING;

        // Set the correct dimension component (will automatically remove the old one)
        this.add(new DimensionComponent(this.dimension));

        // Add a transparent GraphicsComponent to register this entity to the render system (will automatically remove the old one)
        this.add(new GraphicsComponent(Shader.SIMPLE, Texture.NOTHINGNESS, dimension, false));

        // Add an animation component to register this entity to the AnimationSystem
        this.add(new AnimationComponent());

        // Construct the hierarchy of the player
        this.createHierarchy();

        // Add the animations
        this.createAnimations();
    }


    /**
     * Create the entities for the hierarchy
     */
    protected void createHierarchy() {
        // Draw torso to visualise hip position
        torso = new BodyPart(this, this.getHipPosition(), TorsoDimension, 0, Texture.DEBUG);
        torso.add(new PositionComponent(new Vector3f())); // Add position component for the animation
        this.hierarchy.add(torso);

        // Set the position of the foot for the right leg
        Vector3f rightFootOffset = new Vector3f(this.dimension.x / 3, 0.0f, 0.0f);

        // Draw the right leg
        float[] rightLegAngles = this.getLimbAngles(this.getHipPosition(), rightFootOffset, upperLegDimension.y, lowerLegDimension.y, true);
        rightLegUpper = new BodyPart(torso, new Vector3f(), upperLegDimension, rightLegAngles[0], Texture.DEBUG);
        rightLegLower = new BodyPart(rightLegUpper, new Vector3f(0.0f, upperLegDimension.y, 0.0f), lowerLegDimension, rightLegAngles[1], Texture.DEBUG);
        this.hierarchy.add(rightLegUpper);
        this.hierarchy.add(rightLegLower);
        this.IKHandles.put("foot_R", new IKEndEffector(rightLegUpper, rightLegLower, rightFootOffset, "foot_R"));

        // Set the position of the foot for the left leg
        Vector3f leftFootOffset = new Vector3f(this.dimension.x, 0.5f, 0.0f);

        // Draw the left leg
        float[] leftLegAngles = this.getLimbAngles(this.getHipPosition(), leftFootOffset, upperLegDimension.y, lowerLegDimension.y, true);
        leftLegUpper = new BodyPart(torso, new Vector3f(), upperLegDimension, leftLegAngles[0], Texture.WHITE);
        leftLegLower = new BodyPart(leftLegUpper, new Vector3f(0.0f, upperLegDimension.y, 0.0f), lowerLegDimension, leftLegAngles[1], Texture.WHITE);
        this.hierarchy.add(leftLegUpper);
        this.hierarchy.add(leftLegLower);
        this.IKHandles.put("foot_L", new IKEndEffector(leftLegUpper, leftLegLower, leftFootOffset, "foot_L"));

        // Set the right wrist position
        Vector3f rightWristOffset = new Vector3f(this.dimension.x, 1.5f, 0.0f);

        // Draw the right arm
        float[] rightArmAngles = this.getLimbAngles(this.getShoulderPosition(), rightWristOffset, upperArmDimension.y, lowerArmDimension.y, false);
        rightArmUpper = new BodyPart(torso, shoulderOffset, upperArmDimension, rightArmAngles[0], Texture.DEBUG);
        rightArmLower = new BodyPart(rightArmUpper, new Vector3f(0.0f, upperArmDimension.y, 0.0f), lowerArmDimension, rightArmAngles[1], Texture.DEBUG);
        this.hierarchy.add(rightArmUpper);
        this.hierarchy.add(rightArmLower);
        this.IKHandles.put("hand_R", new IKEndEffector(rightArmUpper, rightArmLower, rightWristOffset, "hand_R"));

        // Set the left wrist position
        Vector3f leftWristOffset = new Vector3f(this.dimension.x, 1.5f, 0.0f);

        // Draw the right arm
        float[] leftArmAngles = this.getLimbAngles(this.getShoulderPosition(), rightWristOffset, upperArmDimension.y, lowerArmDimension.y, false);
        leftArmUpper = new BodyPart(torso, shoulderOffset, upperArmDimension, leftArmAngles[0], Texture.WHITE);
        leftArmLower = new BodyPart(leftArmUpper, new Vector3f(0.0f, upperArmDimension.y, 0.0f), lowerArmDimension, leftArmAngles[1], Texture.WHITE);
        this.hierarchy.add(leftArmUpper);
        this.hierarchy.add(leftArmLower);
        this.IKHandles.put("hand_L", new IKEndEffector(leftArmUpper, leftArmLower, leftWristOffset, "arm_L"));
    }


    /**
     * Get the model matrix for the Hierarchical Player (just a translation matrix)
     */
    public Matrix4f getModelMatrix() {
        return Matrix4f.translate(this.getComponent(PositionComponent.class).position);
    }


    /**
     * Calculate the angles of two joints for e.g. a leg or arm based on two set positions and lengths of limb parts
     *
     * @param bodySidePosition the position of the limb on the body side (e.g. the hip position)
     * @param limbEndPosition  the position of the other end of the limb (e.g. the ankle)
     * @param upperLimbLength  the length of the first part of the limb seen from the body (e.g. upper leg)
     * @param lowerLimbLength  the length of the second part of the limb seen from the body (e.g. lower leg)
     * @param bendForward      whether the limb should bend forward or backward (e.g. knee bends in a forward manner)
     * @return float[2] where [0] is the angle of the joint of the bodySidePosition (e.g. hip)
     * and [1] is the angle of the second joint of the limb (e.g. knee)
     */
    // See {root}/images/limbAngles.jpg for a drawing of all angles, most are calculated using the cosine law
    public float[] getLimbAngles(Vector3f bodySidePosition, Vector3f limbEndPosition,
                                 float upperLimbLength, float lowerLimbLength, boolean bendForward) {
        // Array to store the calculated result angles
        float[] result = new float[2];

        // Calculate the offset between the bodySidePosition and the limbEndPosition
        Vector3f offset = limbEndPosition.sub(bodySidePosition);
        float offsetLength = offset.length();

        // Calculate Alpha2
        double alpha2 = Math.toDegrees(Math.atan(Math.abs(offset.x / offset.y)));

        // Calculate Alpha1
        double alpha1;
        if (offsetLength >= upperLimbLength + lowerLimbLength) {
            // For when the two IK bones have to be pointing to the limbEndPosition in 1 straight line
            alpha1 = 0.0f;
        } else {
            alpha1 = Math.toDegrees(Math.acos(
                    (upperLimbLength * upperLimbLength + offsetLength * offsetLength - lowerLimbLength * lowerLimbLength)
                            / (2 * upperLimbLength * offsetLength)));
        }

        // Calculate Alpha0, which is the angle for the bodySidePosition joint, so store it as well
        // See {root}/images/limbAnglesCaseDistinctionForward.jpg and
        // See {root}/images/limbAnglesCaseDistinctionBackward.jpg for a drawing of all below cases
        if (bendForward) {
            if (offset.x > 0) {
                if (offset.y < 0) {
                    result[0] = (float) (180 - alpha1 - alpha2);
                } else {
                    result[0] = (float) (alpha2 - alpha1);
                }
            } else {
                if (offset.y < 0) {
                    result[0] = (float) (180 - alpha1 + alpha2);
                } else {
                    result[0] = (float) (360 - alpha1 - alpha2);
                }
            }
        } else {
            if (offset.x > 0) {
                if (offset.y < 0) {
                    result[0] = (float) (180 + alpha1 - alpha2);
                } else {
                    result[0] = (float) (alpha2 + alpha1);
                }
            } else {
                if (offset.y < 0) {
                    result[0] = (float) (180 + alpha1 + alpha2);
                } else {
                    result[0] = (float) (360 + alpha1 - alpha2);
                }
            }
        }


        // Calculate Beta1
        double beta1;
        if (offsetLength >= upperLimbLength + lowerLimbLength) {
            // For when the two IK bones have to be pointing to the limbEndPosition in 1 straight line
            beta1 = 180.0f;
        } else {
            beta1 = Math.toDegrees(Math.acos(
                    (upperLimbLength * upperLimbLength + lowerLimbLength * lowerLimbLength - offsetLength * offsetLength)
                            / (2 * upperLimbLength * lowerLimbLength)));
        }

        // Calculate Beta0, which is the angle for the second joint, so store it as well
        if (!bendForward) {
            result[1] = (float) -(180 - beta1);
        } else {
            result[1] = (float) (180 - beta1);
        }

        // Return the angle vector
        return result;
    }


    /**
     * Set hip position relative to player bottom left position
     */
    public void setHipPosition(Vector3f newHipPosition) {
        this.hipPosition = newHipPosition;
    }


    /**
     * Get the hip position relative to player bottom left position
     */
    public Vector3f getHipPosition() {
        return this.hipPosition;
    }


    /**
     * Get the torso object of the player
     */
    public BodyPart getTorso() {
        return this.torso;
    }


    /**
     * Get the position of the shoulder relative to the player bottom left position
     */
    public Vector3f getShoulderPosition() {
        return this.hipOffset.add(this.shoulderOffset.rotateXY(-this.torso.rotation));
    }

    private void createAnimations() {

        // Create an animation set for the walking animation
        AnimationSet walkingAnimationSet = this.generateWalkCycle();
        DelayedAnimationSet jumpingAnimationSet = this.generateJumpAnimation();

        // Register the animations within the AnimationComponent
        AnimationComponent ac = this.getComponent(AnimationComponent.class);
        ac.addAnimation(EntityState.PLAYER_WALKING, walkingAnimationSet);
        ac.addAnimation(EntityState.PLAYER_JUMPING, jumpingAnimationSet);
    }

    private AnimationSet generateWalkCycle() {
        // Animate the hip bounce during walking
        SplineAnimation hip = new SplineAnimation(
                this.torso, 0.0f,
                new Vector3f[]{
                        new Vector3f(0.0f, 0.050f, 0.0f),
                        new Vector3f(0.0f, -.083f, 0.0f),
                        new Vector3f(0.0f, -.083f, 0.0f),
                        new Vector3f(0.0f, 0.050f, 0.0f)
                }
        );

        // Add leg animations
        Vector3f[] footPath = new Vector3f[]{
                new Vector3f(0.0f, 0.0f, 0.0f),
                new Vector3f(-.5f, 0.0f, 0.0f),
                new Vector3f(-2.f, 0.0f, 0.0f),
                new Vector3f(-.1f, 0.2f, 0.0f),
                new Vector3f(0.1f, 0.2f, 0.0f),
                new Vector3f(0.7f, 0.0f, 0.0f),
                new Vector3f(0.5f, 0.0f, 0.0f),
                new Vector3f(0.0f, 0.0f, 0.0f)
        };
        SplineAnimation foot_L = new SplineAnimation(this.IKHandles.get("foot_L"), 0.5f, footPath);
        SplineAnimation foot_R = new SplineAnimation(this.IKHandles.get("foot_R"), 0.0f, footPath);

        // Add hand animations
        Vector3f[] handPath = new Vector3f[]{
                new Vector3f(0.0f, 0.4f, 0.0f),
                new Vector3f(-.5f, 0.4f, 0.0f),
                new Vector3f(-2.f, 0.4f, 0.0f),
                new Vector3f(-.1f, 0.4f, 0.0f),
                new Vector3f(0.1f, 0.4f, 0.0f),
                new Vector3f(0.7f, 0.4f, 0.0f),
                new Vector3f(0.5f, 0.4f, 0.0f),
                new Vector3f(0.0f, 0.4f, 0.0f)
        };
        SplineAnimation hand_L = new SplineAnimation(this.IKHandles.get("hand_L"), 0.0f, handPath);
        SplineAnimation hand_R = new SplineAnimation(this.IKHandles.get("hand_R"), 0.5f, handPath);

        // Add the cycles to an animation set and return
        AnimationSet walkingAnimationSet = new AnimationSet();
        walkingAnimationSet.add(hip);
        walkingAnimationSet.add(foot_L);
        walkingAnimationSet.add(foot_R);
        walkingAnimationSet.add(hand_L);
        walkingAnimationSet.add(hand_R);
        return walkingAnimationSet;
    }

    private DelayedAnimationSet generateJumpAnimation() {
        // Animate the hip bounce during walking
        SplineAnimation hipCycle = new SplineAnimation(
                this.torso, 0.0f,
                new Vector3f[]{
                        new Vector3f(0.050f, 0.0f, 0.0f),
                        new Vector3f(-.083f, 0.0f, 0.0f),
                        new Vector3f(-.083f, 0.0f, 0.0f),
                        new Vector3f(0.050f, 0.0f, 0.0f)
                }
        );

//        // Add right leg animation
//        SplineAnimation walkCycle = new SplineAnimation(
//                this.IKHandles.get(0), 0.0f,
//                new Vector3f[]{
//                        new Vector3f(0.0f, 0.0f, 0.0f),
//                        new Vector3f(-.5f, 0.0f, 0.0f),
//                        new Vector3f(-2.f, 0.0f, 0.0f),
//                        new Vector3f(-.1f, 0.2f, 0.0f),
//                        new Vector3f(0.1f, 0.2f, 0.0f),
//                        new Vector3f(0.7f, 0.0f, 0.0f),
//                        new Vector3f(0.5f, 0.0f, 0.0f),
//                        new Vector3f(0.0f, 0.0f, 0.0f)
//                });

        // Add the cycles to an animation set and return
        DelayedAnimationSet jumpingAnimationSet = new DelayedAnimationSet(0.1f);
        jumpingAnimationSet.add(hipCycle);
//        walkingAnimationSet.add(walkCycle);
        return jumpingAnimationSet;
    }

}
