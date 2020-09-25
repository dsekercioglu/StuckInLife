package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.DragonInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.DragonFire;

public class Dragon extends Char {

    public Dragon(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                DragonInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new DragonFire(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
