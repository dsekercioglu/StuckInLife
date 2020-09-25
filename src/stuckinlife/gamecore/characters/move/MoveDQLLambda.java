package stuckinlife.gamecore.characters.move;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.learning.DoubleQLearning;
import utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static stuckinlife.StuckInLife.SEPARATOR;

public class MoveDQLLambda extends MoveController {

    private static final int MOVE_CHOICES = 5;
    private static final double LEARNING_RATE = 1e-6;
    private static final double DISCOUNT_FACTOR = 0.99;
    private static final double REWARD_DECAY = 0.95;
    private static final double EPSILON = 0.3;

    private final String FILE_NAME;
    private final int MOVE_DIR_RESET_TIME;
    private final int NUM_ANGLES = 24;
    private final int NUM_DISTANCES = 20;
    private final int NUM_DIRECTIONS = 4;
    private final double LENGTH;
    private final int[] DIMENSIONS = {NUM_ANGLES, NUM_DISTANCES, NUM_DIRECTIONS};
    private final boolean LEARNING_ON;

    private DoubleQLearning Q_LEARNING;

    private int moveDirResetCounter = 0;


    private final int[] lastState;
    private int action = 4;

    List<Info> infoTracing = new ArrayList<>();
    List<Info> info = new ArrayList<>();

    List<StateAction> replay = new ArrayList<>();


    public MoveDQLLambda(World world, String name, int moveReset, double aiViewRange, boolean learningOn) {
        super(world);
        MOVE_DIR_RESET_TIME = moveReset;
        LENGTH = aiViewRange / NUM_DISTANCES;
        FILE_NAME = "ai" + SEPARATOR + "move" + SEPARATOR + name + "DQLlambda";
        DoubleQLearning model = getModel();
        Q_LEARNING = model;
        LEARNING_ON = learningOn;
        lastState = new int[DIMENSIONS.length];
    }

    public DoubleQLearning getModel() {
        File qLearningA = new File(FILE_NAME);
        DoubleQLearning model = null;
        try {
            if (qLearningA.createNewFile()) {
                model = new DoubleQLearning(DIMENSIONS, MOVE_CHOICES, LEARNING_RATE, DISCOUNT_FACTOR);
            } else {
                model = readModel();
            }
            saveModel(model);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model;
    }

    private DoubleQLearning readModel() {
        DoubleQLearning model = null;
        try (FileInputStream in = new FileInputStream(FILE_NAME);
             ObjectInputStream s = new ObjectInputStream(in)) {
            double[][][] qTable = (double[][][]) s.readObject();
            model = new DoubleQLearning(DIMENSIONS, MOVE_CHOICES, LEARNING_RATE, DISCOUNT_FACTOR);
            model.setQ_TABLE_A(qTable[0]);
            model.setQ_TABLE_B(qTable[1]);
            return model;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveModel(DoubleQLearning model) {
        double[][] qTableAA = model.getQ_TABLE_A();
        double[][] qTableAB = model.getQ_TABLE_B();
        try (FileOutputStream out = new FileOutputStream(FILE_NAME);
             ObjectOutput s = new ObjectOutputStream(out)) {
            double[][][] qTable = {qTableAA, qTableAB};
            s.writeObject(qTable);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reward(double reward, boolean tracing) {
        if (LEARNING_ON) {
            if (tracing) {
                infoTracing.add(new Info(lastState, action, reward));
            } else {
                info.add(new Info(lastState, action, reward));
            }
        }
    }

    @Override
    public void update(int milliseconds) {
        moveDirResetCounter -= milliseconds;
        moveDirResetCounter = Math.max(moveDirResetCounter, 0);
    }

    @Override
    public int[] getMoveDirection() {

        Char closestEnemy = getClosestEnemy();
        if (closestEnemy != null) {
            if (moveDirResetCounter == 0) {
                int[] state = getState(closestEnemy);
                if (Utils.distanceEuclidean(closestEnemy.getX(), closestEnemy.getY(), owner.getX(), owner.getY()) > LENGTH * NUM_DISTANCES) {
                    double angle = Math.atan2(closestEnemy.getY() - owner.getY(), closestEnemy.getX() - owner.getX());
                    action = (int) Math.round(angle * 2 / Math.PI) + 1;
                    while (action < 0) {
                        action += 4;
                    }
                    action %= 4;
                    moveDirResetCounter = (int) (MOVE_DIR_RESET_TIME * Math.random());
                } else {
                    moveDirResetCounter = (int) (Math.random() * MOVE_DIR_RESET_TIME);
                    System.arraycopy(state, 0, lastState, 0, DIMENSIONS.length);
                    if (LEARNING_ON && Math.random() < EPSILON) {
                        action = (int) (Math.random() * MOVE_CHOICES);
                    } else {
                        double[] actions = Q_LEARNING.getActions(state);
                        double[] wallDistances = getWallDistances();
                        action = -1;
                        double highest = Double.NEGATIVE_INFINITY;
                        for (int i = 0; i < MOVE_CHOICES; i++) {
                            if (actions[i] > highest && wallDistances[i] > owner.getMoveSpeed()) {
                                highest = actions[i];
                                action = i;
                            }
                        }
                    }
                    replay.add(new StateAction(state, action));
                    if (LEARNING_ON) {
                        boolean save = false;
                        if (!infoTracing.isEmpty()) {
                            save = true;
                            for (Info info : infoTracing) {
                                double reward = info.REWARD;
                                for (int i = replay.size() - 2; i >= 0; i--) {
                                    StateAction currentStateAction = replay.get(i);
                                    StateAction nextStateAction = replay.get(i + 1);
                                    Q_LEARNING.update(currentStateAction.STATE, currentStateAction.ACTION, reward, nextStateAction.STATE);
                                    reward *= REWARD_DECAY;
                                }
                            }
                            infoTracing.clear();
                        }
                        if (!info.isEmpty()) {
                            save = true;
                            for (Info info : info) {
                                Q_LEARNING.update(info.STATE, info.ACTION, info.REWARD, state);
                            }
                        }
                        info.clear();
                        if (save) {
                            saveModel(Q_LEARNING);
                        }
                    }
                }
            }
        } else {
            action = 4;
        }
        return MOVE_DIR[action];
    }

    public int[] getState(Char enemy) {
        int[] state = new int[DIMENSIONS.length];
        double angleToEnemy = Utils.absoluteAngle(Math.atan2(enemy.getY() - owner.getY(), enemy.getX() - owner.getX()));
        if (angleToEnemy < 0 || angleToEnemy > Math.PI * 2) {
            throw new RuntimeException("angle over math.pi or smaller than zero: " + angleToEnemy);
        }
        int angleIndex = (int) Math.min((angleToEnemy / (Math.PI * 2) * NUM_ANGLES), NUM_ANGLES - 1);
        state[0] = angleIndex;
        double distance = Utils.distanceEuclidean(enemy.getX(), enemy.getY(), owner.getX(), owner.getY());
        state[1] = (int) Math.min((distance / LENGTH), NUM_DISTANCES - 1);
        state[2] = owner.getOrientation();

        return state;
    }

    private Char getClosestEnemy() {
        List<Char> characters = WORLD.getCharacters();

        int closestEnemyIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double currentDistance = Utils.distanceEuclidean(character.getX(), character.getY(), owner.getX(), owner.getY());
                if (currentDistance < lowestDistance) {
                    closestEnemyIndex = i;
                    lowestDistance = currentDistance;
                }
            }
        }

        if (closestEnemyIndex == -1) {
            return null;
        } else {
            return characters.get(closestEnemyIndex);
        }
    }

    public double[] getWallDistances() {
        double[] wallDistances = new double[5];
        for (int i = 0; i < 5; i++) {
            int xDir = MOVE_DIR[i][0];
            int yDir = MOVE_DIR[i][1];
            wallDistances[i] = (xDir == 1) ?
                    (WORLD.getMAX_X() - owner.getX()) :
                    ((xDir == -1) ? (owner.getX() - WORLD.getMIN_X()) :
                            ((yDir == 1) ? (WORLD.getMAX_Y() - owner.getY()) :
                                    ((yDir == -1) ? (owner.getY() - WORLD.getMIN_Y()) :
                                            Double.POSITIVE_INFINITY)));
        }
        return wallDistances;
    }

    private class Info {
        final int[] STATE;
        final int ACTION;
        final double REWARD;

        public Info(int[] state, int action, double reward) {
            STATE = state;
            ACTION = action;
            REWARD = reward;
        }
    }

    private class StateAction {

        final int[] STATE;
        final int ACTION;

        public StateAction(int[] state, int action) {
            STATE = state;
            ACTION = action;
        }
    }
}
