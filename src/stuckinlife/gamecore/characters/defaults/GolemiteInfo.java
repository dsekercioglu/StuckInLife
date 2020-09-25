package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.RANGED;

public class GolemiteInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(240,
                    0,
                    24,
                    40,
                    3000,
                    10000,
                    0.4,
                    12,
                    0.4,
                    RANGED,
                    "Golemite");
        }
        return info;
    }

}
