package stuckinlife.gamecore.characters.charsets.attack;

import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

public class PureAttackManager extends AttackManager {

    public PureAttackManager(Char owner) {
        super(owner);
    }

    @Override
    public void onAttack() {

    }

    public void update(int milliseconds, boolean attacking, boolean tryAttack) {
        double rollDivisor = 600000;
        if (!tryAttack) {
            rollDivisor = 60000;
        }
        if (!attacking) {
            double roll = milliseconds / rollDivisor;
            OWNER.setDeflectionChance(Math.min(OWNER.getDEFLECTION_CHANCE(), OWNER.getDeflectionChance() + roll));
        }
    }
}
