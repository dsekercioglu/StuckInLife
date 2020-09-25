package stuckinlife.gamecore.characters.supercontroller;

public class NullSuperController extends SuperController {

    public NullSuperController() {
        super(null);
    }

    @Override
    public void update(int milliseconds) {

    }

    @Override
    public boolean useSuper() {
        return false;
    }
}
