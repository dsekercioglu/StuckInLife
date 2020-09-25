package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.DefenderController;
import stuckinlife.gamecore.characters.defaults.MagmaSlimeInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.FireSurround;

public class MagmaSlime extends Char {

    public MagmaSlime(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                MagmaSlimeInfo.getInstance(),
                teamCode,
                new DefenderController(world),
                new FireSurround(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
