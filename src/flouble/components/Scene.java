package flouble.components;

import flouble.FloubleApplet;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 * Default width = 160
 * Default height = 100;
 */
public class Scene {

    private final PGraphics P_GRAPHICS;
    private final Pane ROOT;

    public Scene(PGraphics pGraphics, Pane root) {
        P_GRAPHICS = pGraphics;
        ROOT = root;
    }

    public void draw() {
        ROOT.draw(P_GRAPHICS);
    }

    public void update(int deltaTime) {
        ROOT.update(deltaTime);
    }

    public void onClick(MouseEvent mouseEvent) {
        ROOT.onClick(mouseEvent);
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        ROOT.onMouseMoved(mouseEvent.getX(), mouseEvent.getY());
    }
}
