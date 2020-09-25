package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.ReaperController;
import stuckinlife.gamecore.characters.defaults.ReaperInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.ReaperSwipe;

public class Reaper extends Char {

    public Reaper(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                ReaperInfo.getInstance(),
                teamCode,
                new ReaperController(world),
                new ReaperSwipe(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
