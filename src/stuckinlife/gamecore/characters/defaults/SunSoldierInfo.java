package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class SunSoldierInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(300,
                    0,
                    0,
                    20,
                    1500,
                    5000,
                    0,
                    4,
                    0.1,
                    PURE,
                    "SunSoldier");
        }
        return info;
    }

}
