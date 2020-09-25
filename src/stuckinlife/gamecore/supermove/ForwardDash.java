package stuckinlife.gamecore.supermove;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.projectile.DashAttack;

public class ForwardDash extends SuperMove {

    private static final int DURATION_MILLISECONDS = 200;
    private static final double MOVE_SPEED = 540;
    private static final double RANGE = MOVE_SPEED * DURATION_MILLISECONDS / 1000;
    private static final double DASH_WIDTH = 32;
    private static final double DASH_DAMAGE = 90;

    int lastOrientation;

    double normalMoveSpeed;
    double normalDeflectionChance;

    public ForwardDash(World world) {
        super(world, DURATION_MILLISECONDS);
    }


    public void activate() {
        super.activate();
    }

    @Override
    public void onActivation() {
        lastOrientation = owner.getOrientation();
        normalMoveSpeed = owner.getMoveSpeed();
        normalDeflectionChance = owner.getDeflectionChance();
        WORLD.addProjectile(new DashAttack(owner,
                owner.getX(),
                owner.getY(),
                RANGE,
                DASH_WIDTH,
                DASH_DAMAGE,
                lastOrientation,
                owner.getTEAM_CODE()));
        owner.setMoveSpeed(MOVE_SPEED);
        owner.setDeflectionChance(1);
    }

    @Override
    public void complete() {
        owner.setMoveSpeed(normalMoveSpeed);
    }

    @Override
    public int[] getMoveDirection() {
        return MOVE_DIR[lastOrientation];
    }
}
