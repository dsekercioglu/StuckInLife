package stuckinlife.gamecore.characters.defaults;

import stuckinlife.gamecore.characters.charsets.CharacterType;

import static stuckinlife.gamecore.characters.charsets.CharacterType.WEAPONIZED;

public class SoulInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(1,
                    0,
                    0,
                    90,
                    500,
                    5000,
                    0,
                    8,
                    0.4,
                    WEAPONIZED,
                    "Soul");
        }
        return info;
    }

}
