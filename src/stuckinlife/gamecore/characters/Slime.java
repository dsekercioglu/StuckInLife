package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.SlimeInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.SlimeBounce;

public class Slime extends Char {

    public Slime(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                SlimeInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new SlimeBounce(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
