package stuckinlife.gamecore.characters.attack;

public class PlayerAttackController extends AttackController {

    final boolean[] KEYS;

    public PlayerAttackController(boolean[] keys) {
        super(null);
        KEYS = keys;
    }

    @Override
    public boolean update(int milliseconds) {
        return KEYS[4];
    }
}
