package stuckinlife.gamecore.level.charcreator;

import com.google.gson.annotations.Expose;

public class LoopingCharCreator extends CharCreator {

    @Expose
    private final double[] START_POSITION = new double[2];
    @Expose
    private final double[] END_POSITION = new double[2];

    private double[][] positions;

    public LoopingCharCreator(int teamCode,
                              int startTime,
                              int endTime,
                              int numberOfSpawns,
                              double startX,
                              double startY,
                              double endX,
                              double endY,
                              String clazz) {
        super(teamCode, startTime, endTime, numberOfSpawns, clazz);
        START_POSITION[0] = startX;
        START_POSITION[1] = startY;
        END_POSITION[0] = endX;
        END_POSITION[1] = endY;
    }

    @Override
    public void init() {
        double xStep = (END_POSITION[0] - START_POSITION[0]) / (NUMBER_OF_SPAWNS - 1);
        double yStep = (END_POSITION[1] - START_POSITION[1]) / (NUMBER_OF_SPAWNS - 1);
        positions = new double[NUMBER_OF_SPAWNS][2];
        for (int i = 0; i < NUMBER_OF_SPAWNS; i++) {
            positions[i][0] = START_POSITION[0] + i * xStep;
            positions[i][1] = START_POSITION[1] + i * yStep;
        }
    }

    @Override
    public double[] getSpawnLocation(int spawnIndex) {
        return positions[spawnIndex];
    }
}