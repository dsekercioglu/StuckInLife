package flouble.components;

import flouble.background.Background;
import processing.core.PGraphics;
import processing.event.MouseEvent;

import java.util.function.Consumer;

public abstract class InteractiveComponent<T extends Background> extends DynamicComponent<T> {

    private Consumer<MouseEvent> onClickAction;

    public InteractiveComponent(T background) {
        super(background);
    }

    abstract void draw(PGraphics pGraphics);

    public boolean intersects(float mouseX, float mouseY) {
        return getBackground().intersectsPoint(mouseX, mouseY);
    }

    public void onClick(MouseEvent mouseEvent) {
        if (onClickAction != null) {
            onClickAction.accept(mouseEvent);
        }
    }

    public void setOnClick(Consumer<MouseEvent> action) {
        onClickAction = action;
    }
}
