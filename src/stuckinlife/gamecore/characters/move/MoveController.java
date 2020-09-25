package stuckinlife.gamecore.characters.move;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class MoveController {


    protected final int[][] MOVE_DIR = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, 0}};

    protected World WORLD;
    protected Char owner;

    public MoveController(World world) {
        WORLD = world;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public abstract void update(int milliseconds);

    public abstract int[] getMoveDirection();
}
