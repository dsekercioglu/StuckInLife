package flouble.background;

import processing.core.PGraphics;

public abstract class Background {

    public Background() {
    }

    public abstract boolean intersectsPoint(float cursorX, float cursorY);

    public abstract void draw(PGraphics graphics);
 }
