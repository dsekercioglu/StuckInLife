package stuckinlife.gamecore.characters.charsets.attack;

import stuckinlife.gamecore.characters.charsets.Char;

public abstract class AttackManager {

    protected final Char OWNER;

    public AttackManager(Char owner) {
        OWNER = owner;
    }

    public abstract void onAttack();


    public abstract void update(int milliseconds, boolean attacking, boolean tryAttack);
}
