package flouble.background;

import processing.core.PGraphics;
import processing.core.PImage;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;

public class FTImage extends Background {


    private final PImage IMAGE;
    private final int WIDTH;
    private final int HEIGHT;

    private Float[][][] coordinates;

    private final boolean[][] INTERSECTION_MATRIX;

    private Function<Float[][][], Float[][][]> effect;

    private boolean draw = true;

    public FTImage(PImage image) {
        IMAGE = image;
        WIDTH = image.width;
        HEIGHT = image.height;
        coordinates = new Float[WIDTH][HEIGHT][3];
        INTERSECTION_MATRIX = new boolean[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                coordinates[i][j] = new Float[]{i * 1F, j * 1F, 0F};
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
        if (draw) {
            if (effect != null) {
                graphics.strokeWeight(0);
                for (int x = 0; x < WIDTH; x++) {
                    for (int y = 0; y < HEIGHT; y++) {
                        graphics.stroke(IMAGE.get(x, y));
                        graphics.fill(IMAGE.get(x, y));
                        Float[] currentRelativeDrawLocation = coordinates[x][y];
                        graphics.translate(0, 0, currentRelativeDrawLocation[2]);
                        graphics.rect(currentRelativeDrawLocation[0], currentRelativeDrawLocation[1], 1, 1);
                        graphics.translate(0, 0, -currentRelativeDrawLocation[2]);
                    }
                }
            } else {
                graphics.image(IMAGE, 0, 0);
            }
        }
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public void resetEffects() {
        effect = null;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                coordinates[i][j] = new Float[]{i * 1F, j * 1F, 0F};
            }
        }
    }

    public void setEffect(Function<Float[][][], Float[][][]> effect) {
        this.effect = effect;
    }

    public void applyEffect() {
        if (effect != null) {
            coordinates = effect.apply(coordinates);
        }
    }
}
