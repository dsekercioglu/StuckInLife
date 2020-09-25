package stuckinlife.gamecore.characters.move;

public class NullMoveController extends MoveController {

    public NullMoveController() {
        super(null);
    }

    @Override
    public void update(int milliseconds) {

    }

    @Override
    public int[] getMoveDirection() {
        return MOVE_DIR[4];
    }
}
