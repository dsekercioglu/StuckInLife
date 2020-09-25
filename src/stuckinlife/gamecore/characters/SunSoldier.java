package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.SunSoldierInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.SunPower;

public class SunSoldier extends Char {

    public SunSoldier(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                SunSoldierInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new SunPower(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
