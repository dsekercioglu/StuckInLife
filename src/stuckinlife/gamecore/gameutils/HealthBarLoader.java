package stuckinlife.gamecore.gameutils;

import gifAnimation.Gif;
import processing.core.PImage;

import java.awt.*;

public class HealthBarLoader {

    private static final int PIXELS_THRESHOLD = 4;

    public static float[] getHealthBarYLocations(Gif characterGif, Animation[] animations) {
        PImage[] images = characterGif.getPImages();
        float[] healthBarYs = new float[images.length];
        for (int i = 0; i < animations.length; i++) {
            Animation currentAnimation = animations[i];

            int startIndex = currentAnimation.getSTART_INDEX();
            int endIndex = currentAnimation.getEND_INDEX();
            for (int j = startIndex; j <= endIndex; j++) {
                PImage currentImage = images[j];
                int lowestY = 0;

                yloop:
                for (int y = 0; y < currentImage.height; y++) {
                    int numPixels = 0;
                    for (int x = 0; x < currentImage.width; x++) {
                        Color color = new Color(currentImage.get(x, y), true);
                        if (color.getAlpha() != 0) {
                            numPixels++;
                            lowestY = y;
                            if(numPixels > PIXELS_THRESHOLD) {
                                break yloop;
                            }
                        }
                    }
                }
                healthBarYs[j] = lowestY - currentImage.height / 2 - 4;
            }
        }
        return healthBarYs;
    }
}
