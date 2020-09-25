package stuckinlife.gamecore.weapon;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.projectile.InfinityLaser;
import stuckinlife.gamecore.projectile.VenomBall;

public class VenomThrow extends Weapon {

    private static final int DELAY_MILLISECONDS = 0;
    private static final double CRITICAL_CHANCE = 0;

    private static final double VELOCITY = 400;
    private static final double DAMAGE = 20;

    private double attackCountdown;
    private int attackOrientation;

    public VenomThrow(World world) {
        super(world, VELOCITY * 0.2, VELOCITY, "Venom.gif");
        attackCountdown = Double.POSITIVE_INFINITY;
    }

    @Override
    public void update(int milliseconds, boolean attack) {
        if (attack) {
            attackOrientation = owner.getOrientation();
            attackCountdown = DELAY_MILLISECONDS;
        }
        attackCountdown -= milliseconds;
        attackCountdown = Math.max(0, attackCountdown);
        if (attackCountdown == 0) {
            int[] moveDir = MOVE_DIR[attackOrientation];
            WORLD.addProjectile(new VenomBall(owner, owner.getX() + moveDir[0] * 16, owner.getY() + moveDir[1] * 16, VELOCITY, VELOCITY, DAMAGE, CRITICAL_CHANCE, attackOrientation, owner.getTEAM_CODE(), IMAGE));
            attackCountdown = Double.POSITIVE_INFINITY;
        }
    }
}
