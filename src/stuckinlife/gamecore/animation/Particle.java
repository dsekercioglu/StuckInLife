package stuckinlife.gamecore.animation;

import stuckinlife.gamecore.gameutils.Animation;

public class Particle {

    private final double SPEED_DECAY;
    private final int DURATION;
    private final Animation ANIMATION;
    private final double ANGLE;

    private double x;
    private double y;
    private int countdown;

    private double moveSpeed;

    public Particle(double x, double y, double moveSpeed, double angle, double speedDecay, int duration, Animation animation) {
        this.x = x;
        this.y = y;
        this.countdown = duration;
        this.moveSpeed = moveSpeed;
        this.ANGLE = angle;
        SPEED_DECAY = speedDecay;
        DURATION = duration;
        ANIMATION = animation;
    }

    public void update(int milliseconds) {
        x += Math.cos(ANGLE) * moveSpeed;
        y += Math.sin(ANGLE) * moveSpeed;
        moveSpeed *= SPEED_DECAY;
        ANIMATION.update(milliseconds);
        countdown -= milliseconds;
    }

    public boolean isComplete() {
        return countdown <= 0;
    }

    public double getSpeedDecay() {
        return SPEED_DECAY;
    }

    public int getDURATION() {
        return DURATION;
    }

    public String getIMAGE() {
        return ANIMATION.getNAME();
    }

    public double getANGLE() {
        return ANGLE;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public int getAnimationIndex() {
        return ANIMATION.getIndex();
    }
}
