package flouble;

import flouble.components.Scene;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class FloubleApplet extends PApplet {

    private Scene SCENE;

    public void setCurrentScene(Scene scene) {
        SCENE = scene;
    }

    public void draw() {
        clear();
        SCENE.draw();
        SCENE.update((int) (1000 / frameRate));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        SCENE.onClick(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        SCENE.onMouseMoved(mouseEvent);
    }
}
