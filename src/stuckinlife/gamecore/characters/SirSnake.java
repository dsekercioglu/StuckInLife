package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.SirSnakeInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.VenomThrow;

public class SirSnake extends Char {

    public SirSnake(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                SirSnakeInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new VenomThrow(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
