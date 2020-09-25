package stuckinlife.gamecore.animation;

public class Trail {


    private final int ORIENTATION;
    private final int DURATION_MILLISECONDS;
    private final double X;
    private final double Y;
    private final String IMAGE;
    private int countdown;

    public Trail(double x, double y, int orientation, int durationMilliseconds, String image) {
        this.X = x;
        this.Y = y;
        ORIENTATION = orientation;
        DURATION_MILLISECONDS = durationMilliseconds;
        IMAGE = image;
        countdown = DURATION_MILLISECONDS;
    }

    public void update(int milliseconds) {
        countdown -= milliseconds;
    }

    public boolean isComplete() {
        return countdown <= 0;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public int getORIENTATION() {
        return ORIENTATION;
    }

    public int getAnimationIndex() {
        return ORIENTATION;
    }

    public String getIMAGE() {
        return IMAGE;
    }
}
