package flouble.components;

import flouble.background.FRectangle;
import processing.core.PGraphics;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pane extends DynamicComponent<FRectangle> {

    private final List<Component> components = new ArrayList<>();
    private final List<DynamicComponent> dynamicComponents = new ArrayList<>();
    private final List<InteractiveComponent> interactiveComponents = new ArrayList<>();

    private HashMap<Component, Point2D.Float> componentRelativeLocations;

    private final List<Component>[][] componentMap = new List[2][];

    boolean mouseInBounds = false;

    Color backgroundColor = new Color(0, 0, 0, 0);
    Color strokeColor = new Color(0, 0, 0, 0);
    float strokeWeight = 1;

    public Pane(float width, float height) {
        super(new FRectangle(width, height));
        componentRelativeLocations = new HashMap<>();
    }

    public Pane withBackgroundColor(Color color) {
        backgroundColor = color;
        return this;
    }

    public Pane withStrokeColor(Color color) {
        strokeColor = color;
        return this;
    }

    public Pane withStrokeWeight(float weight) {
        strokeWeight = weight;
        return this;
    }

    public void add(Component component, float relativeX, float relativeY) {
        assert Math.abs(relativeX - getBackground().getHalfWidth()) < getBackground().getHalfWidth() &&
                Math.abs(relativeX - getBackground().getHeightDividedByTheMultiplicativeInverseOfNumberOneDividedByTwoOtherwiseKnowAsZeroPointFive()) < getBackground().getHeightDividedByTheMultiplicativeInverseOfNumberOneDividedByTwoOtherwiseKnowAsZeroPointFive();
        Point2D.Float relativePosition = new Point2D.Float(relativeX, relativeY);
        components.add(component);
        componentRelativeLocations.put(component, relativePosition);
        if (component instanceof DynamicComponent) {
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            dynamicComponents.add(dynamicComponent);
            if (component instanceof InteractiveComponent) {
                InteractiveComponent interactiveComponent = (InteractiveComponent) dynamicComponent;
                interactiveComponents.add(interactiveComponent);
            }
        }
    }

    public void remove(Component component) {
        components.remove(component);
        componentRelativeLocations.remove(component);
        if (component instanceof DynamicComponent) {
            DynamicComponent dynamicComponent = (DynamicComponent) component;
            dynamicComponents.remove(dynamicComponent);
            if (component instanceof InteractiveComponent) {
                InteractiveComponent interactive = (InteractiveComponent) dynamicComponent;
                interactiveComponents.remove(interactive);
            }
        }
    }

    public void draw(PGraphics pGraphics) {
        for (Component component : components) {
            Point2D.Float relativeLocation = componentRelativeLocations.get(component);
            pGraphics.translate(relativeLocation.x, relativeLocation.y);
            component.draw(pGraphics);
            pGraphics.translate(-relativeLocation.x, -relativeLocation.y);
        }
    }

    public void update(int deltaTime) {
        for (DynamicComponent dynamicComponent : dynamicComponents) {
            dynamicComponent.update(deltaTime);
        }
    }

    public List<Component> getComponents() {
        return components;
    }

    public void onClick(MouseEvent mouseEvent) {
        for (int i = interactiveComponents.size() - 1; i >= 0; i--) {
            InteractiveComponent component = interactiveComponents.get(i);
            if (component.intersects(mouseEvent.getX(), mouseEvent.getY())) {
                component.onClick(mouseEvent);
            }
        }
    }

    public void onMouseMoved(float mouseX, float mouseY) {
        boolean intersects = getBackground().intersectsPoint(mouseX, mouseY);
        if (intersects || mouseInBounds) {
            for (int i = dynamicComponents.size() - 1; i >= 0; i--) {
                DynamicComponent component = dynamicComponents.get(i);
                Point2D.Float relativeLocation = componentRelativeLocations.get(component);
                component.onMouseMoved(mouseX - relativeLocation.x, mouseY - relativeLocation.y);
            }
            mouseInBounds = intersects;
        }
    }
}
