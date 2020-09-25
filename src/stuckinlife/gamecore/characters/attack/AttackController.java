package stuckinlife.gamecore.characters.attack;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class AttackController {

    protected final World WORLD;
    protected Char owner;

    public AttackController(World world) {
        WORLD = world;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public abstract boolean update(int milliseconds);
}
