package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.CrystaliteInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.CrystaliteGoBoom;

public class Crystalite extends Char {

    public Crystalite(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                CrystaliteInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new CrystaliteGoBoom(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
