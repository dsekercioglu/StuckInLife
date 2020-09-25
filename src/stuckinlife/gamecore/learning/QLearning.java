package stuckinlife.gamecore.learning;

import utils.Utils;

public class QLearning {

    private final int[] DIMS;
    private final double[][] Q_TABLE;
    private final int SIZE;
    private final int ACTIONS;

    final double LEARNING_RATE;
    final double DISCOUNT_FACTOR;

    public QLearning(int[] dims, int actions, double learningRate, double discountFactor) {
        int size = 1;
        for (int i = 0; i < dims.length; i++) {
            size *= dims[i];
        }
        SIZE = size;
        DIMS = dims.clone();
        ACTIONS = actions;
        Q_TABLE = new double[SIZE][ACTIONS];
        LEARNING_RATE = learningRate;
        DISCOUNT_FACTOR = discountFactor;
    }

    private int n21Dim(int[] indices) {
        int n = 1;
        int index = 0;
        for (int i = 0; i < indices.length; i++) {
            index += indices[i] * n;
            n *= DIMS[i];
        }
        return index;
    }

    public void update(int[] state, int action, double reward, int[] nextState) {
        int stateIndex = n21Dim(state);
        Q_TABLE[stateIndex][action] *= 1 - LEARNING_RATE;
        Q_TABLE[stateIndex][action] += LEARNING_RATE * (reward + DISCOUNT_FACTOR * Utils.max(Q_TABLE[n21Dim(nextState)]));
    }

    public double[] getActions(int[] state) {
        return Q_TABLE[n21Dim(state)];
    }

    public double[][] getQ_TABLE() {
        return Q_TABLE;
    }

    public void setQ_TABLE(double[][] qTable) {
        System.arraycopy(qTable, 0, Q_TABLE, 0, qTable.length);
    }

    public int[] getDIMS() {
        return DIMS;
    }
}
