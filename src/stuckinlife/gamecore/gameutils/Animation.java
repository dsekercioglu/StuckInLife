package stuckinlife.gamecore.gameutils;

public class Animation {

    private final int START_INDEX;
    private final int END_INDEX;
    private final int LENGTH;
    private final int MILLISECONDS;
    private final boolean LOOP;
    private final boolean DISRUPTIVE;
    private final String NAME;

    private int counter = 0;

    private int index;

    public Animation(int startIndex, int endIndex, int milliseconds, boolean loop, boolean disruptive, String name) {
        START_INDEX = startIndex;
        index = START_INDEX;
        END_INDEX = endIndex;
        LENGTH = END_INDEX - START_INDEX;
        MILLISECONDS = milliseconds;
        LOOP = loop;
        DISRUPTIVE = disruptive;
        NAME = name;
    }

    public void reset() {
        counter = 0;
    }

    public void update(int milliseconds) {
        if(counter / MILLISECONDS <= LENGTH) {
            counter += milliseconds;
        } else if(LOOP) {
            reset();
        }
        index = Math.min((START_INDEX + (counter / MILLISECONDS)), END_INDEX);
    }

    public int getIndex() {
        return index;
    }

    public boolean animationDone() {
        boolean animationDone = counter / MILLISECONDS > LENGTH;
        return animationDone || LOOP || !DISRUPTIVE;
    }

    public int getSTART_INDEX() {
        return START_INDEX;
    }

    public int getEND_INDEX() {
        return END_INDEX;
    }

    public int getLENGTH() {
        return LENGTH;
    }

    public int getMILLISECONDS() {
        return MILLISECONDS;
    }

    public boolean isLOOP() {
        return LOOP;
    }

    public String getNAME() {
        return NAME;
    }

    public boolean isDISRUPTIVE() {
        return DISRUPTIVE;
    }
}


