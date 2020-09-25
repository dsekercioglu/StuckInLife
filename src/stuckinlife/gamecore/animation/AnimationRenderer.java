package stuckinlife.gamecore.animation;

import stuckinlife.gamecore.gameutils.Animation;

public class AnimationRenderer {

    private final double X;
    private final double Y;
    private final String NAME;
    private final Animation ANIMATION;

    public AnimationRenderer(double x, double y, String name, Animation animation) {
        X = x;
        Y = y;
        NAME = name;
        ANIMATION = animation;
    }

    public void update(int milliSeconds) {
        ANIMATION.update(milliSeconds);
    }

    public boolean isComplete() {
        return ANIMATION.animationDone();
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public String getNAME() {
        return NAME;
    }

    public int getAnimationIndex() {
        return ANIMATION.getIndex();
    }
}
