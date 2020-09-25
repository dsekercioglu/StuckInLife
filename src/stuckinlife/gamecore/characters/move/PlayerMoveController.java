package stuckinlife.gamecore.characters.move;

import java.util.ArrayList;
import java.util.List;

public class PlayerMoveController extends MoveController {


    private final boolean[] KEYS;
    private final boolean[] OLD_KEYS;

    private final int LENGTH = 4;

    public PlayerMoveController(boolean[] keys) {
        super(null);
        KEYS = keys;
        OLD_KEYS = new boolean[6];
    }


    @Override
    public void update(int milliseconds) {

    }

    @Override
    public int[] getMoveDirection() {
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < LENGTH; i++) {
            if (KEYS[i]) {
                keys.add(i);
            }
        }
        if (keys.size() == 1) {
            System.arraycopy(KEYS, 0, OLD_KEYS, 0, KEYS.length);
            return MOVE_DIR[keys.get(0)];
        } else if (keys.size() > 1) {
            for (int i = 0; i < LENGTH; i++) {
                if (KEYS[i] != OLD_KEYS[i] && KEYS[i]) {
                    return MOVE_DIR[i];
                }
            }
        }
        System.arraycopy(KEYS, 0, OLD_KEYS, 0, KEYS.length);
        return new int[]{0, 0};
    }
}
