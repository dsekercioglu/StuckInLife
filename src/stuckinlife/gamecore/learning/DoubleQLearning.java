package stuckinlife.gamecore.learning;

import utils.Utils;

import java.io.Serializable;

public class DoubleQLearning implements Serializable {

    private final int[] DIMS;
    private final double[][] Q_TABLE_A;
    private final double[][] Q_TABLE_B;
    private final int SIZE;
    private final int ACTIONS;

    final double LEARNING_RATE;
    final double DISCOUNT_FACTOR;

    public DoubleQLearning(int[] dims, int actions, double learningRate, double discountFactor) {
        int size = 1;
        for (int i = 0; i < dims.length; i++) {
            size *= dims[i];
        }
        SIZE = size;
        DIMS = dims.clone();
        ACTIONS = actions;
        Q_TABLE_A = new double[SIZE][ACTIONS];
        Q_TABLE_B = new double[SIZE][ACTIONS];
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
        if(Math.random() < 0.5) {
            int nextAction = Utils.argmax(Q_TABLE_A[stateIndex]);
            Q_TABLE_A[stateIndex][action] *= 1 - LEARNING_RATE;
            Q_TABLE_A[stateIndex][action] += LEARNING_RATE * (reward + DISCOUNT_FACTOR * Q_TABLE_B[n21Dim(nextState)][nextAction]);
        } else {
            int nextAction = Utils.argmax(Q_TABLE_B[stateIndex]);
            Q_TABLE_B[stateIndex][action] *= 1 - LEARNING_RATE;
            Q_TABLE_B[stateIndex][action] += LEARNING_RATE * (reward + DISCOUNT_FACTOR * Q_TABLE_A[n21Dim(nextState)][nextAction]);
        }
    }

    public double[] getActions(int[] state) {
        return Utils.add(Q_TABLE_A[n21Dim(state)], Q_TABLE_B[n21Dim(state)]);
    }

    public double[][] getQ_TABLE_A() {
        return Q_TABLE_A;
    }

    public double[][] getQ_TABLE_B() {
        return Q_TABLE_B;
    }

    public void setQ_TABLE_A(double[][] qTable) {
        System.arraycopy(qTable, 0, Q_TABLE_A, 0, qTable.length);
    }

    public void setQ_TABLE_B(double[][] qTable) {
        System.arraycopy(qTable, 0, Q_TABLE_B, 0, qTable.length);
    }

    public int[] getDIMS() {
        return DIMS;
    }
}
