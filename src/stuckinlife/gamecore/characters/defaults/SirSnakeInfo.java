package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.RANGED;

public class SirSnakeInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(50,
                    0,
                    0,
                    60,
                    900,
                    150,
                    0,
                    12,
                    0.4,
                    RANGED,
                    "SirSnake");
        }
        return info;
    }

}
