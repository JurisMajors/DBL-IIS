package group4.levelSystem.modules;

import group4.ECS.entities.Camera;
import group4.ECS.entities.world.Platform;
import group4.ECS.entities.world.SplinePlatform;
import group4.graphics.Shader;
import group4.graphics.Texture;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.maths.Vector3f;
import group4.maths.spline.MultiSpline;

public class TestModule extends Module {

    public TestModule(Level l) {
        super(l);
    }

    public TestModule(Level l, String modelName) {
        super(l, modelName);
    }

    @Override
    protected void constructModule() {
        // TODO: This is a bad spot for this, but it demonstrates the functionality. Please move.
        Camera camera = new Camera();
        this.addEntity(camera); // Adding the camera to the module (which adds it to the engine?)

        // TODO: Change to platform entities when these work
        // Construct the test module for training the neural network

        // Add the LHS wall
        Vector3f tempPosition = new Vector3f();
        Vector3f tempDimension = new Vector3f(2.0f, 28.0f, 0.0f);
        Platform LHSWall = new Platform(tempPosition, tempDimension, Shader.SIMPLE, Texture.BRICK);
        this.addEntity(LHSWall);

        // my little spline test
        tempPosition = new Vector3f(4.0f, 4.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 1.0f, 0.0f);
        float thickness = 0.4f;
        Vector3f[] tempPoint = new Vector3f[]{
                new Vector3f().add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(2.0f, 0.0f, 0.0f),
                new Vector3f(2.0f, 2.0f, 0.0f),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(4.0f, 2.0f, 0.0f).sub(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
                new Vector3f(6.0f, 2.0f, 0.0f),
                new Vector3f(6.0f, 0.0f, 0.0f),
                new Vector3f(8.0f, 0.0f, 0.0f).add(new Vector3f(0.0f, thickness * 0.5f, 0.0f)),
        };
        for (Vector3f v : tempPoint) {
//            v.addi(new Vector3f(4.0f, 4.0f, 0.0f));
        }

        MultiSpline mySpline = new MultiSpline(tempPoint);
        SplinePlatform splinePlatform = new SplinePlatform(tempPosition, tempDimension, mySpline, 0.4f, Shader.SIMPLE, Texture.WHITE);
        this.addEntity(splinePlatform);

        // Add floor on which player will stand initially
        tempPosition = new Vector3f(2.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(20.0f, 2.0f, 0.0f);
        Platform initFloor = new Platform(tempPosition, tempDimension);
        this.addEntity(initFloor);

        // Add some blocks to potentially confuse the player on initial floor
        tempPosition = new Vector3f(2.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Platform confusion = new Platform(tempPosition, tempDimension);
        this.addEntity(confusion);

        // Add a block for the player to jump over
        tempPosition = new Vector3f(9.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Platform jump = new Platform(tempPosition, tempDimension);
        this.addEntity(jump);

        tempPosition = new Vector3f(13.0f, 4.0f, 0.0f);
        tempDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Platform idk = new Platform(tempPosition, tempDimension);
        this.addEntity(idk);

        // Create semi-stair like thing
        tempPosition = new Vector3f(22.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(5.0f, 2.0f, 0.0f);
        Platform stairlike = new Platform(tempPosition, tempDimension);
        this.addEntity(stairlike);

        tempPosition = new Vector3f(27.0f, 4.0f, 0.0f);
        tempDimension = new Vector3f(8.0f, 2.0f, 0.0f);
        stairlike = new Platform(tempPosition, tempDimension);
        this.addEntity(stairlike);

        tempPosition = new Vector3f(35.0f, 6.0f, 0.0f);
        tempDimension = new Vector3f(5.0f, 2.0f, 0.0f);
        stairlike = new Platform(tempPosition, tempDimension);
        this.addEntity(stairlike);

        // Create floor to jump on to
        tempPosition = new Vector3f(44.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(8.0f, 2.0f, 0.0f);
        Platform jumpFloor = new Platform(tempPosition, tempDimension);
        this.addEntity(jumpFloor);

        // Create a hole to jump over
        tempPosition = new Vector3f(54.0f, 0.0f, 0.0f);
        tempDimension = new Vector3f(6.0f, 2.0f, 0.0f);
        Platform afterHole = new Platform(tempPosition, tempDimension);
        this.addEntity(afterHole);

        // Create height increase to module end exit
        tempPosition = new Vector3f(60.0f, 2.0f, 0.0f);
        tempDimension = new Vector3f(4.0f, 2.0f, 0.0f);
        Platform exitStairs = new Platform(tempPosition, tempDimension);
        this.addEntity(exitStairs);

        // Create the exit
        Vector3f exitPosition = new Vector3f(62.0f, 4.0f, 0.0f);
        Vector3f exitDimension = new Vector3f(2.0f, 2.0f, 0.0f);
        Platform exit = new Platform(exitPosition, exitDimension);
        this.addEntity(exit);
        // TODO: Change to exit entity, but first need to know how collision detection is going to work to detect if player overlaps an exit, before I create an exit entity
    }

    @Override
    protected Vector3f getStartScreenWindow() {
        return new Vector3f();
    }

    @Override
    public Vector3f getPlayerInitialPosition() {
        return new Vector3f(6.0f, 2.0f, 0.0f);
    }
}

