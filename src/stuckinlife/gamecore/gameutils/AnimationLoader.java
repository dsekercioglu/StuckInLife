package stuckinlife.gamecore.gameutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static stuckinlife.StuckInLife.MAIN_PATH;
import static stuckinlife.StuckInLife.SEPARATOR;

public class AnimationLoader {

    public static final int NUM_ANIMATIONS = 18;

    public static Animation[] loadCharAnimations(String characterName) {
        String fileName = MAIN_PATH + "anim" + SEPARATOR + characterName;
        Animation[] animations = new Animation[NUM_ANIMATIONS];
        int index = 0;
        try {
            File animationTxt = new File(fileName + ".txt");
            Scanner reader = new Scanner(animationTxt);
            int i = 0;
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] data = line.split(", ");
                Animation animation = new Animation(Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        Boolean.parseBoolean(data[4]),
                        Boolean.parseBoolean(data[5]),
                        data[0]);
                animations[index] = animation;
                index++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return animations;
    }
}
