package stuckinlife.gamecore.characters.supercontroller;

public class PlayerSuperController extends SuperController{

    private final boolean[] KEYS;

    public PlayerSuperController(boolean[] keys) {
        super(null);
        KEYS = keys;
    }

    @Override
    public void update(int milliseconds) {

    }

    @Override
    public boolean useSuper() {
        return KEYS[5];
    }
}
