package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class CrystaliteInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(1,
                    0,
                    0,
                    90,
                    0,
                    150,
                    0,
                    1,
                    0,
                    PURE,
                    "Crystalite");
        }
        return info;
    }

}
