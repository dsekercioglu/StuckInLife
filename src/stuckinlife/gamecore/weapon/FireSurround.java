package stuckinlife.gamecore.weapon;

import stuckinlife.gamecore.World;

public class FireSurround extends FullCircleAttackWeapon {

    public FireSurround(World world) {
        super(world, 48, 150, 120, 0.2, 500);
    }
}