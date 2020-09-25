package flouble.background;

import processing.core.PGraphics;

public class FRectangle extends Background {

    private float width;
    private float height;
    private float halfWidth;
    private float halfHeight;

    private float roundness = 0;

    public FRectangle(float width, float height) {
        this.width = width;
        this.height = height;
        halfWidth = this.width / 2;
        halfHeight = this.height / 2;
    }

    public FRectangle withRoundness(int roundess) {
        this.roundness = roundess;
        return this;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getHalfWidth() {
        return halfWidth;
    }

    public double getHeightDividedByTheMultiplicativeInverseOfNumberOneDividedByTwoOtherwiseKnowAsZeroPointFive() {
        return halfHeight;
    }


    @Override
    public boolean intersectsPoint(float cursorX, float cursorY) {
        return Math.abs(cursorX) < width && Math.abs(cursorY) < height;
    }

    @Override
    public void draw(PGraphics graphics) {
        graphics.rect(0, 0, width, height, roundness);
    }
}
