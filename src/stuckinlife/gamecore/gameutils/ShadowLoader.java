package stuckinlife.gamecore.gameutils;

import gifAnimation.Gif;
import processing.core.PImage;

import java.awt.*;

public class ShadowLoader {

    private static final int IDLE_ANIMATIONS = 4;

    public static int[][] getShadowLocations(Gif characterGif, Animation[] animations) {
        PImage[] images = characterGif.getPImages();
        int[][] shadows = new int[images.length][3];


        int maxLength = 0;
        for (int i = 0; i < animations.length; i++) {
            Animation currentAnimation = animations[i];

            int idleMaxLength = 0;

            int startIndex = currentAnimation.getSTART_INDEX();
            int endIndex = currentAnimation.getEND_INDEX();


            int highestY = 0;

            boolean calculateShadowLength = i < IDLE_ANIMATIONS;
            for (int j = startIndex; j <= endIndex; j++) {
                PImage currentImage = images[j];
                if (j == startIndex) {
                    int startX = currentImage.width;
                    int endX = 0;
                    for (int y = 0; y < currentImage.height; y++) {
                        for (int x = 0; x < currentImage.width; x++) {
                            Color color = new Color(currentImage.get(x, y), true);
                            if (color.getAlpha() != 0) {
                                if (calculateShadowLength) {
                                    startX = Math.min(x, startX);
                                    endX = Math.max(x, endX);
                                }
                                highestY = Math.max(highestY, y);
                            }
                        }
                        if (calculateShadowLength) {
                            int length = endX - startX;
                            idleMaxLength = Math.max(length, idleMaxLength);
                        }
                    }
                }
                maxLength = Math.max(maxLength, idleMaxLength);
                shadows[j][1] = highestY - currentImage.height / 2;
            }
        }
        for (int i = 0; i < shadows.length; i++) {
            shadows[i][2] = maxLength * 2;
        }
        return shadows;
    }
}
