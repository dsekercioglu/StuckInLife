package flouble.components;

import flouble.background.Background;
import processing.core.PGraphics;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class DynamicComponent<T extends Background> extends Component {

    private T background;

    private BiConsumer<Float, Float> onMouseEnterAction;
    private BiConsumer<Float, Float> onMouseExitAction;
    private Consumer<Integer> onUpdateAction;

    boolean previouslyIntersecting = false;

    public DynamicComponent(T background) {
        this.background = background;
    }

    abstract void draw(PGraphics pGraphics);


    public T getBackground() {
        return background;
    }

    public void setBackground(T background) {
        this.background = background;
    }

    public boolean intersects(float mouseX, float mouseY) {
        return background.intersectsPoint(mouseX, mouseY);
    }

    public void update(int deltaTime) {
        if (onUpdateAction != null) {
            onUpdateAction.accept(deltaTime);
        }
    }

    public void onMouseMoved(float mouseX, float mouseY) {
        boolean mouseWithinBounds = intersects(mouseX, mouseY);
        if (mouseWithinBounds && !previouslyIntersecting) {
            onMouseEnter(mouseX, mouseY);
            previouslyIntersecting = true;
        } else if (!mouseWithinBounds && previouslyIntersecting) {
            onMouseExit(mouseX, mouseY);
            previouslyIntersecting = false;
        }
    }

    public void setOnUpdate(Consumer<Integer> action) {
        onUpdateAction = action;
    }

    public void setOnMouseEnter(BiConsumer<Float, Float> action) {
        onMouseEnterAction = action;
    }

    public void setOnMouseExit(BiConsumer<Float, Float> action) {
        onMouseExitAction = action;
    }

    public void onMouseEnter(float mouseX, float mouseY) {
        if (onMouseEnterAction != null) {
            onMouseEnterAction.accept(mouseX, mouseY);
        }
    }

    public void onMouseExit(float mouseX, float mouseY) {
        if (onMouseExitAction != null) {
            onMouseExitAction.accept(mouseX, mouseY);
        }
    }
}
