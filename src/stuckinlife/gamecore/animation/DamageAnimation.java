package stuckinlife.gamecore.animation;

import stuckinlife.StuckInLife;

import java.awt.*;

public class DamageAnimation {

    private static final int STD_DURATION = 1000;
    private static final int CRIT_DURATION = 1500;
    private static final double SPEED_DECAY = 0.85;
    private final Color COLOR;
    private final double ANGLE;
    private final String TEXT;
    private final int SIZE_INDEX;

    private double x;
    private double y;
    private int countdown;

    private double moveSpeed = 6;

    public DamageAnimation(double x, double y, double damage, boolean critical) {
        boolean deflected = damage == 0;
        COLOR = deflected ? Color.YELLOW : Color.RED;
        countdown = !deflected && critical ? CRIT_DURATION : STD_DURATION;
        this.x = x;
        this.y = y;
        ANGLE = Math.random() * Math.PI * 2;
        int formattedDamage = (int) damage;
        TEXT = deflected ? "DEFLECTED" + (critical ? " CRIT" : "") : ((critical ? "CRIT " : "") + formattedDamage);
        SIZE_INDEX = Math.min((formattedDamage / 10), StuckInLife.NUM_TEXT_FONTS - 1);
    }

    public void update(int milliseconds) {
        x += Math.cos(ANGLE) * moveSpeed;
        y += Math.sin(ANGLE) * moveSpeed;
        moveSpeed *= Math.pow(SPEED_DECAY, moveSpeed);
        countdown -= milliseconds;
    }

    public boolean isComplete() {
        return countdown <= 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getTEXT() {
        return TEXT;
    }

    public int getSIZE_INDEX() {
        return SIZE_INDEX;
    }

    public Color getCOLOR() {
        return COLOR;
    }
}
