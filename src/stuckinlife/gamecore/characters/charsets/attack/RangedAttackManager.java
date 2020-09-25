package stuckinlife.gamecore.characters.charsets.attack;

import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

public class RangedAttackManager extends AttackManager {

    public RangedAttackManager(Char owner) {
        super(owner);
    }

    @Override
    public void onAttack() {
        OWNER.setDeflectionChance(OWNER.getDEFLECTION_CHANCE());
    }

    public void update(int milliseconds, boolean attacking, boolean tryAttack) {
        double rollDivisor = 100000;
        if (!tryAttack) {
            rollDivisor = 10000;
        }
        if (!attacking) {
            double roll = milliseconds / rollDivisor;
            OWNER.setDeflectionChance(Math.min(1, OWNER.getDeflectionChance() + roll));
        }
    }
}
