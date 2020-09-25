package stuckinlife.gamecore.level;

import com.google.gson.annotations.Expose;
import stuckinlife.gamecore.World;
import stuckinlife.gamecore.level.charcreator.CharCreator;

import java.util.ArrayList;
import java.util.List;

public class Level {

    @Expose
    private final List<CharCreator> CHAR_CREATORS;

    public Level(List<CharCreator> charCreators) {
        CHAR_CREATORS = charCreators;
    }

    public void init() {
        for (CharCreator charCreator : CHAR_CREATORS) {
            charCreator.init();
        }
    }

    public void update(World world, int milliseconds) {
        List<CharCreator> charCreatorsToRemove = new ArrayList<>();
        for (CharCreator charCreator : CHAR_CREATORS) {
            if (charCreator.update(world, milliseconds)) {
                charCreatorsToRemove.add(charCreator);
            }
        }
        CHAR_CREATORS.removeAll(charCreatorsToRemove);
        charCreatorsToRemove.clear();
    }
}
