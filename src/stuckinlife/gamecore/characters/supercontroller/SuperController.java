package stuckinlife.gamecore.characters.supercontroller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class SuperController {

    protected final World WORLD;
    protected Char owner;

    public SuperController(World world) {
        WORLD = world;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public abstract void update(int milliseconds);

    public abstract boolean useSuper();
}
