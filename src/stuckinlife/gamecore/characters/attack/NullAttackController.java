package stuckinlife.gamecore.characters.attack;

public class NullAttackController extends AttackController {

    public NullAttackController() {
        super(null);
    }

    @Override
    public boolean update(int milliseconds) {
        return false;
    }
}
