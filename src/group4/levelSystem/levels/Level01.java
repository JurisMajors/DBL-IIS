package group4.levelSystem.levels;

import group4.ECS.entities.HierarchicalPlayer;
import group4.ECS.entities.Player;
import group4.ECS.entities.mobs.ShootingJumpingWalkingMob;
import group4.ECS.entities.world.Exit;
import group4.ECS.etc.TheEngine;
import group4.levelSystem.ExitAction;
import group4.levelSystem.Level;
import group4.levelSystem.Module;
import group4.levelSystem.modules.TestModule;
import group4.maths.Vector3f;

public class Level01 extends Level {

    @Override
    protected Module createRoot() {
        return new Module(this, "./src/group4/res/maps/level_01/module_01_01.json", null);
    }

    @Override
    protected void createAdditionalModules() {
        // Add the SimpleModule as well (Tiled version)
//        this.addModule();
    }

    @Override
    protected Player createPlayer() {
        // We don't care about the player position, as that will be initialized on level switching
        return new HierarchicalPlayer(new Vector3f(), this);
    }

    @Override
    protected void configExits() {
        // We need to configure all exits
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
    }

}
