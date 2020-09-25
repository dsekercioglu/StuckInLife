package stuckinlife.gamecore.characters.defaults;

import stuckinlife.gamecore.characters.charsets.CharacterType;

import static stuckinlife.gamecore.characters.charsets.CharacterType.WEAPONIZED;

public class PlayerInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(200,
                    0,
                    0,
                    90,
                    400,
                    250,
                    0,
                    8,
                    0.4,
                    WEAPONIZED,
                    "ArmoredPlayer");
        }
        return info;
    }

}
