package stuckinlife.gamecore.characters;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.controller.PlayerController;
import stuckinlife.gamecore.characters.defaults.PlayerInfo;
import stuckinlife.gamecore.supermove.ForwardDash;
import stuckinlife.gamecore.weapon.Sword;

public class Player extends Char implements Playable {

    public Player(World world, double x, double y, int orientation, int teamCode, boolean[] keys) {
        super(world,
                x,
                y,
                orientation,
                PlayerInfo.getInstance(),
                teamCode,
                new PlayerController(keys),
                new Sword(world),
                new ForwardDash(world));
    }

    @Override
    public void onDeath(Char enemy) {
        //reincarnate();
    }

    @Override
    public void onKill(Char enemy) {

    }

    public void reincarnate() {
        health = MAX_HEALTH;
        WORLD.addCharacter(this);
        WORLD.addCharacter(new Soul(WORLD, x, y, orientation, TEAM_CODE));
    }
}
