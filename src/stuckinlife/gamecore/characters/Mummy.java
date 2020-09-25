package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.MummyInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.MummyEyes;

public class Mummy extends Char {

    public Mummy(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                MummyInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new MummyEyes(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
