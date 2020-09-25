package stuckinlife.gamecore.projectile;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public abstract class Projectile {

    protected static final int[][] SHOOT_DIR = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    protected final Char OWNER;
    protected final double RANGE;
    protected final double START_X;
    protected final double START_Y;
    protected final int ORIENTATION;
    protected final double CRITICAL_CHANCE;
    protected final int TEAM_CODE;
    protected final String IMAGE;


    protected double angle;
    protected double velocity;
    protected double x;
    protected double y;
    protected double distanceTraveled;


    public Projectile(Char owner,
                      double x,
                      double y,
                      int orientation,
                      double range,
                      double velocity,
                      double criticalChance,
                      int teamCode,
                      String image) {
        OWNER = owner;
        START_X = x;
        START_Y = y;
        ORIENTATION = orientation;
        this.x = x;
        this.y = y;
        this.angle = orientation * Math.PI / 2;
        RANGE = range;
        this.velocity = velocity;
        CRITICAL_CHANCE = criticalChance;
        TEAM_CODE = teamCode;
        IMAGE = image;
    }

    public abstract void update(World world, double milliseconds);

    public abstract boolean destroy();

    public abstract boolean hit(double x, double y);

    public abstract double getDamage(Char character);

    public Char getOWNER() {
        return OWNER;
    }

    public double getRANGE() {
        return RANGE;
    }

    public String getIMAGE() {
        return IMAGE;
    }

    public int getTEAM_CODE() {
        return TEAM_CODE;
    }

    public double getAngle() {
        return angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public int getORIENTATION() {
        return ORIENTATION;
    }

    public double getCRITICAL_CHANCE() {
        return CRITICAL_CHANCE;
    }
}
