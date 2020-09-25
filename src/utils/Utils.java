package utils;

public class Utils {

    private static double TWO_PI = Math.PI * 2;

    public static double distanceEuclidean(double x0, double y0, double x1, double y1) {
        return Math.hypot(x0 - x1, y0 - y1);
    }

    public static double distanceChebyshev(double x0, double y0, double x1, double y1) {
        return Math.max(Math.abs(x0 - x1), Math.abs(y0 - y1));
    }

    public static double distanceManhattan(double x0, double y0, double x1, double y1) {
        return Math.abs(x0 - x1) + Math.abs(y0 - y1);
    }

    public static double relativeAngle(double angle) {
        angle %= TWO_PI;
        if (angle >= 0) {
            return (angle < Math.PI) ? angle : angle - TWO_PI;
        } else {
            return angle >= -Math.PI ? angle : angle + TWO_PI;
        }
    }

    public static double absoluteAngle(double angle) {
        angle %= TWO_PI;
        if (angle >= 0) {
            return angle;
        } else {
            return angle + TWO_PI;
        }
    }

    public static double min(double... n) {
        double min = n[0];
        for (int i = 1; i < n.length; i++) {
            min = Math.min(min, n[i]);
        }
        return min;
    }

    public static int min(int... n) {
        int min = n[0];
        for (int i = 1; i < n.length; i++) {
            min = Math.min(min, n[i]);
        }
        return min;
    }

    public static double max(double... n) {
        double max = n[0];
        for (int i = 1; i < n.length; i++) {
            max = Math.max(max, n[i]);
        }
        return max;
    }

    public static int max(int... n) {
        int max = n[0];
        for (int i = 1; i < n.length; i++) {
            max = Math.max(max, n[i]);
        }
        return max;
    }

    public static int argmin(double... n) {
        double min = n[0];
        int index = 0;
        for (int i = 1; i < n.length; i++) {
            if (n[i] < min) {
                min = n[i];
                index = i;
            }
        }
        return index;
    }

    public static int argmax(double... n) {
        double max = n[0];
        int index = 0;
        for (int i = 1; i < n.length; i++) {
            if (n[i] > max) {
                max = n[i];
                index = i;
            }
        }
        return index;
    }

    public static double hypot(double... n) {
        double dist = 0;
        for (int i = 0; i < n.length; i++) {
            dist += sq(n[i]);
        }
        return Math.sqrt(dist);
    }

    public static double hypot(int... n) {
        double dist = 0;
        for (int i = 0; i < n.length; i++) {
            dist += Math.pow(n[i], 2);
        }
        return Math.round(Math.sqrt(dist));
    }

    public static double[] random(int size, double min, double max) {
        double[] d = new double[size];
        for (int i = 0; i < size; i++) {
            d[i] = (Math.random() * (max - min)) + min;
        }
        return d;
    }

    public static int[] random(int size, int min, int max) {
        int[] d = new int[size];
        for (int i = 0; i < size; i++) {
            d[i] = (int) ((Math.random() * (max - min)) + min);
        }
        return d;
    }

    public static int[] copy(int[] d0) {
        int[] d1 = new int[d0.length];
        for (int i = 0; i < d1.length; i++) {
            d1[i] = d0[i];
        }
        return d1;
    }

    public static double[] copy(double[] d0) {
        double[] d1 = new double[d0.length];
        for (int i = 0; i < d1.length; i++) {
            d1[i] = d0[i];
        }
        return d1;
    }

    public static double[] add(double[] d1, double[] d2) {
        for (int i = 0; i < d1.length; i++) {
            d1[i] += d2[i];
        }
        return d1;
    }

    public static double euclidean(int[] d1, int[] d2) {
        double dist = 0;
        for (int i = 0; i < Math.min(d1.length, d2.length); i++) {
            dist += Math.pow(d1[i] - d2[i], 2);
        }
        return Math.sqrt(dist);
    }

    public static double sum(double... d) {
        double sum = 0;
        for (int i = 0; i < d.length; i++) {
            sum += d[i];
        }
        return sum;
    }

    public static double mean(double... d) {
        return sum(d) / d.length;
    }

    public static double variance(double... d) {
        double mean = mean(d);
        double variance = 0;
        for (int i = 0; i < d.length; i++) {
            variance += sq(mean - d[i]);
        }
        return variance;
    }

    public static double stdDev(double... d) {
        return Math.sqrt(variance(d));
    }

    public static double sq(double x) {
        return x * x;
    }

    public static double cb(double x) {
        return x * x * x;
    }
}
