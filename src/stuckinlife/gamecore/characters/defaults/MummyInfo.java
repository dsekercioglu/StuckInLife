package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.PURE;

public class MummyInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(200,
                    0,
                    0,
                    40,
                    1000,
                    150,
                    0,
                    8,
                    0.4,
                    PURE,
                    "Mummy");
        }
        return info;
    }

}
