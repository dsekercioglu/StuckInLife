package stuckinlife.menutest;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.List;

public class MenuMain extends PApplet {

    private int WIDTH;
    private int HEIGHT;

    public static final int MIN_FONT_SIZE = 5;
    public static final int MAX_FONT_SIZE = 120;
    public static final int NUM_FONT_SIZE = MAX_FONT_SIZE - MIN_FONT_SIZE;
    private PFont[] fonts = new PFont[NUM_FONT_SIZE];

    MainMenu mainMenu;

    public void settings() {
        fullScreen(P2D);
        WIDTH = displayWidth;
        HEIGHT = displayHeight;
        mainMenu = new MainMenu();
    }

    public void setup() {
        for (int i = 5; i < NUM_FONT_SIZE; i++) {
            PFont font = createFont("font/04B_11__.TTF", i);
            fonts[i] = font;
        }
    }

    public void draw() {
        clear();
        List<Button> buttons = mainMenu.getButtons();
        for (Button button : buttons) {
            float centerX = button.getCENTER_X() * WIDTH;
            float centerY = button.getCENTER_Y() * HEIGHT;
            float width = button.getWIDTH() * WIDTH;
            float height = button.getHEIGHT() * HEIGHT;
            int rgb = button.getColor().getRGB();
            stroke(0);
            fill(rgb);
            rect(centerX - width / 2, centerY - height / 2, width, height);
            fill(255F);
            Label label = button.getLABEL();
            textAlign(CENTER, CENTER);
            textFont(fonts[label.getFontSize() - MIN_FONT_SIZE]);
            text(label.getText(), centerX, centerY);
        }
    }

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[]{MenuMain.class.getName()};
        if (appletArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
