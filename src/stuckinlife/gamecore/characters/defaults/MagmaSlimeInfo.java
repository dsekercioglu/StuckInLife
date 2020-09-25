package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class MagmaSlimeInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(400,
                    0,
                    12,
                    10,
                    3000,
                    5000,
                    0.8,
                    40,
                    0.1,
                    PURE,
                    "MagmaSlime");
        }
        return info;
    }

}
