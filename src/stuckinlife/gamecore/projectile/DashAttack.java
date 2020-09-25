package stuckinlife.gamecore.projectile;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;

public class DashAttack extends Projectile {

    private final double DAMAGE;
    private final double DASH_CROSS_SECTION;

    public DashAttack(Char owner,
                      double x,
                      double y,
                      double range,
                      double dashCrossSection,
                      double damage,
                      int orientation,
                      int teamCode) {
        super(owner, x, y, orientation, range, range, 0, teamCode, "Empty.gif");
        DASH_CROSS_SECTION = dashCrossSection;
        DAMAGE = damage;
    }

    @Override
    public void update(World world, double milliseconds) {
        x = START_X + SHOOT_DIR[ORIENTATION][0] * distanceTraveled;
        y = START_Y + SHOOT_DIR[ORIENTATION][1] * distanceTraveled;
        distanceTraveled += velocity;
    }

    @Override
    public boolean destroy() {
        return RANGE <= distanceTraveled;
    }

    @Override
    public boolean hit(double x, double y) {
        int xDir = SHOOT_DIR[ORIENTATION][0];
        int yDir = SHOOT_DIR[ORIENTATION][1];

        double yDistance = Math.abs(getY() - y);
        double xDistance = Math.abs(getX() - x);
        boolean yInDashZone = yDistance < DASH_CROSS_SECTION;
        boolean yInRange = yDistance < distanceTraveled;
        boolean xInDashZone = xDistance < DASH_CROSS_SECTION;
        boolean xInRange = xDistance < distanceTraveled;

        if (getX() * xDir < x * xDir && yInDashZone && xInRange) {
            return true;
        }
        return getY() * yDir < y * yDir && xInDashZone && yInRange;
    }

    @Override
    public double getDamage(Char character) {
        return DAMAGE;
    }
}
