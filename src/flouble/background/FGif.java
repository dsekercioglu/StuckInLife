package flouble.background;

import processing.core.PGraphics;
import processing.core.PImage;

public class FGif extends Background {


    private final FImage[] IMAGES;
    private int currentImage = 0;

    public FGif(PImage[] gif) {
        IMAGES = new FImage[gif.length];
        for (int i = 0; i < IMAGES.length; i++) {
            IMAGES[i] = new FImage(gif[i]);
        }
    }

    public void setCurrentImage(int index) {
        currentImage = index;
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public void nextImage() {
        currentImage++;
        currentImage %= IMAGES.length;
    }

    @Override
    public boolean intersectsPoint(float cursorX, float cursorY) {
        return IMAGES[currentImage].intersectsPoint(cursorX, cursorY);
    }

    @Override
    public void draw(PGraphics graphics) {
        IMAGES[currentImage].draw(graphics);
    }
}
