package stuckinlife.gamecore.weapon;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.projectile.WideAttack;

public abstract class WideAttackWeapon extends Weapon {


    private final int DELAY_MILLISECONDS;
    private final double CRITICAL_CHANCE;

    private final double START_DAMAGE;
    private final double END_DAMAGE;

    private double deflectionChance;
    private double attackCountdown;

    private int attackOrientation;


    public WideAttackWeapon(World world, double range, double startDamage, double endDamage, double criticalChance, int delayMilliseconds) {
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
            attackOrientation = owner.getOrientation();
            attackCountdown = DELAY_MILLISECONDS;
            deflectionChance = owner.getDeflectionChance();
        }
        attackCountdown -= milliseconds;
        attackCountdown = Math.max(0, attackCountdown);
        if (attackCountdown == 0) {
            WORLD.addProjectile(new WideAttack(owner,
                    owner.getX(),
                    owner.getY(),
                    RANGE,
                    START_DAMAGE,
                    END_DAMAGE,
                    CRITICAL_CHANCE,
                    attackOrientation,
                    owner.getTEAM_CODE()));
            attackCountdown = Double.POSITIVE_INFINITY;
        }
    }
}
