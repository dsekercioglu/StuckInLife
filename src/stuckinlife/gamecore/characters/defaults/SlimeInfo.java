package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class SlimeInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(100,
                    0,
                    0,
                    60,
                    1000,
                    10000,
                    0,
                    1,
                    1,
                    PURE,
                    "Slime");
        }
        return info;
    }

}
