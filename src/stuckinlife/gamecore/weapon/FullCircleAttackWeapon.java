package stuckinlife.gamecore.weapon;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.projectile.FullCircleAttack;

public abstract class FullCircleAttackWeapon extends Weapon {


    private final int DELAY_MILLISECONDS;
    private final double CRITICAL_CHANCE;
    private double attackCountdown;

    private final double START_DAMAGE;
    private final double END_DAMAGE;

    public FullCircleAttackWeapon(World world, double range, double startDamage, double endDamage, double criticalChance, int delayMilliseconds) {
        super(world, range, range, "Empty");
        START_DAMAGE = startDamage;
        END_DAMAGE = endDamage;
        CRITICAL_CHANCE = criticalChance;
        DELAY_MILLISECONDS = delayMilliseconds;
        attackCountdown = Double.POSITIVE_INFINITY;
    }

    @Override
    public void update(int milliseconds, boolean attack) {
        if (attack) {
            attackCountdown = DELAY_MILLISECONDS;
        }
        attackCountdown -= milliseconds;
        attackCountdown = Math.max(0, attackCountdown);
        if (attackCountdown == 0) {
            WORLD.addProjectile(new FullCircleAttack(owner, owner.getX(), owner.getY(), RANGE, START_DAMAGE, END_DAMAGE, CRITICAL_CHANCE, owner.getTEAM_CODE()));
            attackCountdown = Double.POSITIVE_INFINITY;
        }
    }
}
