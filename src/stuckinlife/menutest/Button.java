package stuckinlife.menutest;

import java.awt.*;

public class Button {

    private final Label LABEL;
    private final float CENTER_X;
    private final float CENTER_Y;
    private final float WIDTH;
    private final float HEIGHT;

    private boolean enabled;

    private final Color ENABLED_COLOR = Color.GRAY;
    private final Color NORMAL_COLOR = Color.RED;

    private Color color;

    public Button(Label label, float centerX, float centerY, float width, float height) {
        LABEL = label;
        CENTER_X = centerX;
        CENTER_Y = centerY;
        WIDTH = width;
        HEIGHT = height;
        enable();
    }

    public void enable() {
        enabled = true;
        color = ENABLED_COLOR;
    }

    public void disable() {
        enabled = false;
        color = NORMAL_COLOR;
    }

    public void hover(boolean hover) {
        if (enabled) {
            System.out.println("hover: " + hover + " " + LABEL);
        }
    }

    public void onAction() {
        if (enabled) {
            System.out.println("onAction: " + LABEL);
        }
    }

    public Color getColor() {
        return color;
    }

    public float getCENTER_X() {
        return CENTER_X;
    }

    public float getCENTER_Y() {
        return CENTER_Y;
    }

    public float getWIDTH() {
        return WIDTH;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }

    public Label getLABEL() {
        return LABEL;
    }
}
