package stuckinlife.gamecore.supermove;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class SuperMove {

    protected final World WORLD;
    protected Char owner;

    private final int DURATION_MILLISECONDS;
    private int durationCounter;

    protected final int[][] MOVE_DIR = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {0, 0}};

    boolean superMoveCompleted = true;

    public SuperMove(World world, int durationMilliseconds) {
        WORLD = world;
        DURATION_MILLISECONDS = durationMilliseconds;
        durationCounter = 0;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public boolean active() {
        return durationCounter != 0;
    }

    public void activate() {
        if (durationCounter == 0) {
            superMoveCompleted = false;
            durationCounter = DURATION_MILLISECONDS;
            onActivation();
        }
    }

    public abstract void onActivation();

    public abstract void complete();

    public abstract int[] getMoveDirection();

    public void update(int milliseconds) {
        durationCounter -= milliseconds;
        durationCounter = Math.max(durationCounter, 0);
        if (!superMoveCompleted && durationCounter == 0) {
            superMoveCompleted = true;
            complete();
        }
    }

}
