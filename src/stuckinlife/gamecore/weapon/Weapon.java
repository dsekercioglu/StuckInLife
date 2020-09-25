package stuckinlife.gamecore.weapon;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class Weapon {

    protected static final int[][] MOVE_DIR = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {0, 0}};

    protected final World WORLD;
    protected final double RANGE;
    protected final double VELOCITY;
    protected final String IMAGE;
    protected Char owner;

    public Weapon(World world, double range, double velocity, String image) {
        WORLD = world;
        RANGE = range;
        VELOCITY = velocity;
        IMAGE = image;
    }

    public void setOwner(Char owner) {
        this.owner = owner;
    }

    public abstract void update(int milliseconds, boolean attack);

    public double getRANGE() {
        return RANGE;
    }

    public double getVELOCITY() {
        return VELOCITY;
    }

    public String getIMAGE() {
        return IMAGE;
    }
}
