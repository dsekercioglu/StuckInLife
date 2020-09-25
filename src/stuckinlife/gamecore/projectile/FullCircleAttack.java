package stuckinlife.gamecore.projectile;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

public class FullCircleAttack extends Projectile {

    private final double START_DAMAGE;
    private final double DAMAGE_DIFFERENCE;

    public FullCircleAttack(Char owner,
                            double x,
                            double y,
                            double range,
                            double startDamage,
                            double endDamage,
                            double criticalChance,
                            int teamCode) {
        super(owner, x, y, 0, range, range, criticalChance, teamCode, "Empty.gif");
        START_DAMAGE = startDamage;
        DAMAGE_DIFFERENCE = endDamage - startDamage;
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
        return Utils.distanceEuclidean(x, y, START_X, START_Y) < distanceTraveled;
    }

    @Override
    public double getDamage(Char character) {
        return START_DAMAGE + DAMAGE_DIFFERENCE * Utils.distanceEuclidean(character.getX(), character.getY(), START_X, START_Y) / RANGE;
    }
}
