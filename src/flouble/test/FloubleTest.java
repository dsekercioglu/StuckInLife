package flouble.test;

import flouble.FloubleApplet;
import flouble.background.FImage;
import flouble.components.*;
import gifAnimation.Gif;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PGraphicsOpenGL;

public class FloubleTest extends FloubleApplet {

    /*
    boolean clicked = false;
    int currentTimer = 0;
    int timer = 0;
     */

    boolean update = false;

    public static void main(String[] args) {
        FloubleApplet.main(FloubleTest.class.getName());
    }

    public void setup() {
        float[] d = new float[]{2, 22, 97, 159};
        for (double de : d) {
            System.out.println(de / (2 + 22 + 97 + 159 + 2 + 22 + 97));
        }
        ((PGraphicsOpenGL) g).textureSampling(2);
        Pane pane = new Pane(800, 500);
        String directory = "img" + "/" + "chars" + "/";
        Gif gif = new Gif(this, directory + "Dragon.gif");
        PImage[] frames = gif.getPImages();
        PGraphics[] pGraphics = new PGraphics[frames.length];

        FImage[] images = new FImage[frames.length];
        for (int j = 0; j < frames.length; j++) {
            pGraphics[j] = createGraphics(frames[j].width * 2, frames[j].height * 2);
            pGraphics[j].beginDraw();
            pGraphics[j].image(frames[j], 0, 0, 128, 128);
            pGraphics[j].endDraw();
            images[j] = new FImage(frames[j]);
        }

        //Label label = new Label(0, 0, "Test", Color.WHITE, 30F);
        //Background rectangle = new Rectangle(300F, 300F, 300F, 300F).withRoundness(20);

        ToggleButton<FImage> toggleButton = new ToggleButton<>(images);
        toggleButton.setOnMouseEnter((x, y) -> {
            update = true;
        });

        toggleButton.setOnMouseExit((x, y) -> {
            update = false;
        });

        toggleButton.setOnUpdate((deltaTime) -> {
            if (update) {
                toggleButton.next();
            }
        });
        /*
        FTImage ftimg = new FTImage(400F, 250F, pGraphics[0]);
        Button<FTImage> button = new Button<>(ftimg);

        ftimg.setEffect(FloubleTest::explosion);

        button.setOnClick((mouseEvent -> {
            clicked = true;
            timer = 0;
        }));
        button.setOnUpdate((deltaTime) -> {
            if (clicked) {
                if (currentTimer <= 0) {
                    button.getBackground().applyEffect();
                    currentTimer = 100;
                }
                if (timer > 2000) {
                    button.getBackground().setDraw(false);
                }
                currentTimer -= deltaTime;
                timer += deltaTime;
            }
        });

        button.setOnMouseEnter((x, y) -> {
            cursor(CROSS);
        });

        button.setOnMouseExit((x, y) -> {
            cursor(ARROW);
        });
         */

        pane.add(toggleButton, 400, 250);
        Scene scene = new Scene(this.g, pane);
        setCurrentScene(scene);
    }

    private static Float[][][] explosion(Float[][][] previous) {
        int width = previous.length;
        int height = previous[0].length;
        Float[][][] Fekt = new Float[width][height][3];
        for (int x = 0; x < Fekt.length; x++) {
            for (int y = 0; y < Fekt.length; y++) {
                Float[] newCoordinates = previous[x][y].clone();
                newCoordinates[0] -= width / 2F;
                newCoordinates[1] -= height / 2F;
                newCoordinates[0] *= 1.01F * (float) (Math.random() * 1.5 + 0.5);
                newCoordinates[1] *= 1.05F * (float) (Math.random() * 1.5 + 0.5);
                newCoordinates[2] += 25F * (float) (Math.random() * 1.5 + 0.5);
                newCoordinates[0] += width / 2F;
                newCoordinates[1] += height / 2F;
                Fekt[x][y] = newCoordinates;
            }
        }
        return Fekt;
    }

    public void settings() {
        size(800, 500, P3D);
    }
}
