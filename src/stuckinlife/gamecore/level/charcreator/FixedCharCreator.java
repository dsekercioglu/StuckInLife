package stuckinlife.gamecore.level.charcreator;

import com.google.gson.annotations.Expose;
import stuckinlife.gamecore.level.charcreator.CharCreator;

public class FixedCharCreator extends CharCreator {

    @Expose
    private final double[] POSITION;

    public FixedCharCreator(int teamCode,
                            int startTime,
                            int endTime,
                            int numberOfSpawns,
                            double x,
                            double y,
                            String clazz) {
        super(teamCode, startTime, endTime, numberOfSpawns, clazz);
        POSITION = new double[]{x, y};
    }

    @Override
    public void init() {

    }

    @Override
    public double[] getSpawnLocation(int spawnIndex) {
        return POSITION;
    }
}
