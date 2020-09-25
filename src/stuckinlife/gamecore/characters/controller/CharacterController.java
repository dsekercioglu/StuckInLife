package stuckinlife.gamecore.characters.controller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class CharacterController {

    protected final int[][] MOVE_DIR = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {0, 0}};

    protected World WORLD;
    protected Char owner;

    public CharacterController(World world) {
        WORLD = world;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public abstract void update(int milliseconds);

    public abstract int[] getMoveDirection();

    public abstract boolean attack();

    public abstract boolean useSuper();
}
