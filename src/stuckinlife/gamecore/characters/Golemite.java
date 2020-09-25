package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.GolemiteController;
import stuckinlife.gamecore.characters.defaults.GolemiteInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.GolemiteEye;

public class Golemite extends Char {


    public Golemite(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                GolemiteInfo.getInstance(),
                teamCode,
                new GolemiteController(world),
                new GolemiteEye(world),
                new ForwardDash(world));
    }


    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
