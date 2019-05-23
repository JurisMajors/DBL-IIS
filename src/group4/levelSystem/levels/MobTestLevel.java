package group4.levelSystem.levels;

import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.*;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.TheEngine;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

public class MobTestLevel extends Level {

    @Override
    protected Module createRoot() {
        return new TestModule(this, null);
    }

    @Override
    protected void createAdditionalModules() {
        // For now I'll not add additional modules to this level
        return;
    }

    @Override
    protected Player createPlayer() {
        //temporary add mob to the level here
//        TheEngine.getInstance().addEntity(new ShootingJumpingWalkingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
//        TheEngine.getInstance().addEntity(new FlappingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
//        TheEngine.getInstance().addEntity(new JumpingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
//        TheEngine.getInstance().addEntity(new JumpingWalkingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
//        TheEngine.getInstance().addEntity(new WalkingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));
        TheEngine.getInstance().addEntity(new FlyingMob(new Vector3f(15.0f, 10.0f, 0.0f), this));

        // We don't care about the player position, as that will be initialized on level switching
        return new Player(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        // The MobTestLevel uses the SimpleModule, which doesn't have exits, so we do not need to configure them
        ExitAction global = new ExitAction() {
            @Override
            public void exit() {
                System.out.println("Reached the exit :-)");
            }
        };

        for (Module m : this.modules) {
            for (Exit e : m.getExits()) {
                this.setExitAction(e, global);
            }
        }

        return;
    }

}
