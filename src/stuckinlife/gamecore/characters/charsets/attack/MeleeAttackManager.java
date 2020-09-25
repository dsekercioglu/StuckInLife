package stuckinlife.gamecore.characters.charsets.attack;

import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

public class MeleeAttackManager extends AttackManager {

    public MeleeAttackManager(Char owner) {
        super(owner);
    }

    @Override
    public void onAttack() {
        OWNER.setDeflectionChance(OWNER.getDeflectionChance() + Utils.cb(1 - Math.max(OWNER.getDeflectionChance(), 0)));
    }

    public void update(int milliseconds, boolean attacking, boolean tryAttack) {
        double rollDivisor = 10000;
        if (!tryAttack) {
            rollDivisor = 1000;
        }
        if (!attacking) {
            double roll = milliseconds / rollDivisor;
            OWNER.setDeflectionChance(Math.max(OWNER.getDEFLECTION_CHANCE(), OWNER.getDeflectionChance() - roll));
        }
    }
}
