package flouble.background;

import processing.core.PGraphics;
import processing.core.PImage;

import java.awt.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FImage extends Background {


    private final PImage IMAGE;
    private final int WIDTH;
    private final int HEIGHT;

    private final boolean[][] INTERSECTION_MATRIX;

    public FImage(PImage image) {
        IMAGE = image;
        WIDTH = image.width;
        HEIGHT = image.height;
        INTERSECTION_MATRIX = new boolean[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                INTERSECTION_MATRIX[i][j] = new Color(image.get(i, j), true).getAlpha() != 0;
            }
        }
    }

    @Override
    public boolean intersectsPoint(float cursorX, float cursorY) {
        int adjustedX = Math.round(cursorX);
        int adjustedY = Math.round(cursorY);
        return (adjustedX < WIDTH && adjustedX >= 0 && adjustedY < HEIGHT && adjustedY >= 0) && INTERSECTION_MATRIX[adjustedX][adjustedY];
    }

    @Override
    public void draw(PGraphics graphics) {
        graphics.image(IMAGE, 0, 0);
    }
}
