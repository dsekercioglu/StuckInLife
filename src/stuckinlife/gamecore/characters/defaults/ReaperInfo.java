package stuckinlife.gamecore.characters.defaults;

import static stuckinlife.gamecore.characters.charsets.CharacterType.WEAPONIZED;

public class ReaperInfo {

    private static CharInfo info = null;

    public static CharInfo getInstance() {
        if (info == null) {
            info = new CharInfo(90,
                    0,
                    0,
                    70,
                    600,
                    150,
                    0,
                    4,
                    0.8,
                    WEAPONIZED,
                    "Reaper");
        }
        return info;
    }

}
