package stuckinlife.gamecore.level.charcreator;

import com.google.gson.annotations.Expose;
import stuckinlife.gamecore.World;
import stuckinlife.gamecore.gameutils.CharFactory;

public abstract class CharCreator {

    private static final String CHAR_PACKAGE = "stuckinlife.gamecore.characters.";

    @Expose
    protected final int TEAM_CODE;

    @Expose
    protected final int START_TIME;

    @Expose
    protected final int NUMBER_OF_SPAWNS;

    @Expose
    String CHAR_CLASS;

    @Expose
    protected final int SPAWN_TIME_INTERVAL;


    protected int time = 0;

    protected int spawnIndex = 0;

    public CharCreator(int teamCode, int startTime, int spawnTimeInterval, int numberOfSpawns, String clazz) {
        TEAM_CODE = teamCode;
        START_TIME = startTime;
        SPAWN_TIME_INTERVAL = spawnTimeInterval;
        NUMBER_OF_SPAWNS = numberOfSpawns;
        CHAR_CLASS = clazz;
    }

    public abstract void init();

    public abstract double[] getSpawnLocation(int spawnIndex);


    public boolean update(World world, int milliseconds) {
        time += milliseconds;
        if (SPAWN_TIME_INTERVAL == 0) {
            double startX = world.getMIN_X();
            double startY = world.getMIN_Y();
            double xField = world.getMAX_X() - startX;
            double yField = world.getMAX_Y() - startY;
            for (int i = 0; i < NUMBER_OF_SPAWNS; i++) {
                double[] spawnLocation = getSpawnLocation(i);
                try {
                    CharFactory.createAndAdd(world,
                            Class.forName(CHAR_PACKAGE + CHAR_CLASS),
                            TEAM_CODE,
                            startX + spawnLocation[0] * xField,
                            startY + spawnLocation[1] * yField,
                            1);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
            int currentSpawnIndex = (int) (1D * (time - START_TIME) / SPAWN_TIME_INTERVAL * (NUMBER_OF_SPAWNS));
            if (spawnIndex < NUMBER_OF_SPAWNS && currentSpawnIndex >= spawnIndex) {
                double startX = world.getMIN_X();
                double startY = world.getMIN_Y();
                double xField = world.getMAX_X() - startX;
                double yField = world.getMAX_Y() - startY;
                for (int i = spawnIndex; i < Math.min(currentSpawnIndex + 1, NUMBER_OF_SPAWNS); i++) {
                    double[] spawnLocation = getSpawnLocation(i);
                    try {
                        CharFactory.createAndAdd(world,
                                Class.forName(CHAR_PACKAGE + CHAR_CLASS),
                                TEAM_CODE,
                                startX + spawnLocation[0] * xField,
                                startY + spawnLocation[1] * yField,
                                1);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    spawnIndex++;
                }
            }
            return spawnIndex >= NUMBER_OF_SPAWNS;
        }
    }
}
