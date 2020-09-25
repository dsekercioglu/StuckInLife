package flouble.components;

import flouble.background.Background;
import processing.core.PGraphics;

import java.awt.*;

public class Button<T extends Background> extends InteractiveComponent<T> {

    Label label = null;

    public Button(Label label, T background) {
        super(background);
        this.label = label;
    }

    public Button(T background) {
        super(background);
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }

    @Override
    void draw(PGraphics pGraphics) {
        getBackground().draw(pGraphics);
        if (label != null) {
            label.draw(pGraphics);
        }
    }
}
