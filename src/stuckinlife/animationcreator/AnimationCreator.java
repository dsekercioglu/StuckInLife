package stuckinlife.animationcreator;

import gifAnimation.Gif;
import processing.core.PApplet;
import processing.core.PImage;
import stuckinlife.gamecore.gameutils.Animation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class AnimationCreator extends PApplet {

    private final static String SEPARATOR = File.separator;

    private final static String GIF_PATH = "img/chars/Dragon.gif";
    private final static String ANIMATION_PATH = "anim/Dragon.txt";

    private final int KEY_PRESS_TIME = 500;
    private int keyPressCountdown = 0;

    private int WIDTH;
    private int HEIGHT;
    private int WORLD_WIDTH = 840;
    private int WORLD_HEIGHT = 525;
    private float WORLD_IMG_SIZE = 2.5F;
    private float WORLD_RATIO;


    Map<String, Animation> animationMap = new HashMap<>();
    Gif gif = null;

    List<String> animationTypes = new ArrayList<>();

    int index = 0;

    long time;


    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{AnimationCreator.class.getName()};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }


    public void settings() {
        fullScreen();
        noSmooth();
        WIDTH = displayWidth;
        HEIGHT = displayHeight;
        WORLD_RATIO = Math.min(WIDTH * 1F / WORLD_WIDTH, HEIGHT * 1F / WORLD_HEIGHT);
        loadChar(GIF_PATH, ANIMATION_PATH);
    }

    public void setup() {
        time = System.currentTimeMillis();
    }

    public void draw() {
        int deltaTime = (int) (System.currentTimeMillis() - time);
        clear();
        rect(0, 0, WIDTH, HEIGHT);
        if(gif != null) {
            Animation animation = animationMap.get(animationTypes.get(index));
            animation.update(deltaTime);
            int index = animation.getIndex();
            PImage img = gif.getPImages()[index];
            drawCostume(img, WIDTH / 2, HEIGHT / 2, img.width * WORLD_IMG_SIZE * WORLD_RATIO);
        }
        keyPressCountdown -= deltaTime;
        keyPressCountdown = Math.max(keyPressCountdown, 0);
        time = System.currentTimeMillis();
    }

    public void keyPressed() {
        if(keyPressCountdown <= 0) {
            if (keyCode == RIGHT) {
                index += 1;
            } else if (keyCode == LEFT) {
                index -= 1;
            } else if(keyCode == ENTER) {
                loadChar(GIF_PATH, ANIMATION_PATH);
            }
            index += animationTypes.size();
            index %= animationTypes.size();
            keyPressCountdown = KEY_PRESS_TIME;
        }
    }

    public void loadChar(String gifPath, String animationPath) {
        gif = new Gif(this, gifPath);
        animationMap.clear();
        animationTypes.clear();
        try {
            File myObj = new File(animationPath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                String[] data = line.split(", ");
                Animation animation = new Animation(Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),
                        Integer.parseInt(data[3]),
                        true,
                        false,
                        data[0]);
                animationTypes.add(data[0]);
                animationMap.put(animation.getNAME(), animation);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void drawCostume(PImage costume, float centerX, float centerY, float size) {
        float newSize = size * WORLD_IMG_SIZE;
        imageMode(CENTER);
        translate(centerX, centerY);
        image(costume, 0, 0, newSize, newSize);
        translate(-centerX, -centerY);
    }
}
