package stuckinlife.gamecore.projectile;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.animation.Trail;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

public class InfinityLaser extends Projectile {

    private final int LIFE_TIME = 500;
    private int countdown = LIFE_TIME;

    private final int X_DIR;
    private final int Y_DIR;

    private boolean hit = false;

    private final double DAMAGE;

    public InfinityLaser(Char owner,
                         double x,
                         double y,
                         double range,
                         double velocity,
                         double damage,
                         double criticalChance,
                         int orientation,
                         int teamCode,
                         String image) {
        super(owner, x, y, orientation, range, velocity, criticalChance, teamCode, image);
        DAMAGE = damage;
        X_DIR = SHOOT_DIR[orientation][0];
        Y_DIR = SHOOT_DIR[orientation][1];
    }

    @Override
    public void update(World world, double milliseconds) {
        world.addTrail(new Trail(x, y, ORIENTATION, countdown, IMAGE));
        x += X_DIR * velocity * milliseconds / 1000;
        y += Y_DIR * velocity * milliseconds / 1000;
        countdown -= milliseconds;
    }

    @Override
    public boolean destroy() {
        return countdown < 0 || hit;
    }

    @Override
    public boolean hit(double x, double y) {
        return !hit && (hit = Utils.distanceEuclidean(x, y, this.x, this.y) < 16);
    }

    @Override
    public double getDamage(Char character) {
        return DAMAGE;
    }
}
