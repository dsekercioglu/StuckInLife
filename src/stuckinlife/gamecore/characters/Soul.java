package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.FollowController;
import stuckinlife.gamecore.characters.defaults.SoulInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.Sword;

public class Soul extends Char implements Playable {

    public Soul(World world, double x, double y, int orientation, int teamCode) {
        super(world,
                x,
                y,
                orientation,
                SoulInfo.getInstance(),
                teamCode,
                new FollowController(world),
                new Sword(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {

    }

    @Override
    public void onKill(Char enemy) {

    }
}
