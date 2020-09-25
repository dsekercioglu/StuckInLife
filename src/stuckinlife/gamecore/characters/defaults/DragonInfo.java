package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class DragonInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(1500,
                    0,
                    8,
                    40,
                    2000,
                    10000,
                    0,
                    12,
                    0.4,
                    PURE,
                    "Dragon");
        }
        return info;
    }

}
