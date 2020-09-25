package stuckinlife.menutest;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {

    float mouseX;
    float mouseY;

    protected List<Button> buttons = new ArrayList<>();

    public List<Button> getButtons() {
        return buttons;
    }

    public void setMousePosition(float mouseX, float mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
