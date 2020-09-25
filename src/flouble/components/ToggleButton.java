package flouble.components;

import flouble.background.Background;

import java.util.function.BiConsumer;

public class ToggleButton<T extends Background> extends Button<T> {

    private final int NUMBER_OF_STATES;
    private final T[] BACKGROUNDS;
    private final Label[] LABELS;
    int state;

    @SafeVarargs
    public ToggleButton(Label[] labels, T... backgrounds) {
        super(labels == null ? null : labels[0], backgrounds[0]);
        assert labels == null || labels.length == backgrounds.length;
        NUMBER_OF_STATES = backgrounds.length;
        BACKGROUNDS = backgrounds;
        if (labels == null) {
            LABELS = new Label[NUMBER_OF_STATES];
        } else {
            LABELS = labels.clone();
        }
    }

    @SafeVarargs
    public ToggleButton(T... backgrounds) {
        this(null, backgrounds);
    }

    public void setState(int state) {
        this.state = state;
        setBackground(BACKGROUNDS[state]);
        setLabel(LABELS[state]);
    }

    public void next() {
        setState((state + 1) % NUMBER_OF_STATES);
    }

    public int getState() {
        return state;
    }

    public int numberOfStates() {
        return NUMBER_OF_STATES;
    }
}
