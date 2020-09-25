package flouble.components;

import processing.core.PGraphics;

import java.awt.*;

import static processing.core.PConstants.CENTER;

public class Label extends Component {

    private static final float AD_FACTOR = 0.8F;

    String text;
    Color color;
    float fontSize;

    public Label(String text, Color color, float fontSize) {
        this.text = text;
        this.color = color;
        this.fontSize = fontSize;
    }

    void draw(PGraphics graphics) {
        graphics.textSize(fontSize);
        graphics.textAlign(CENTER, CENTER);
        float ascent = graphics.textAscent() * AD_FACTOR;
        float descent = graphics.textDescent() * AD_FACTOR;
        graphics.textSize(fontSize + (ascent - descent));
        graphics.fill(color.getRGB());
        graphics.text(text, 0, -descent);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }
}
