package stuckinlife.menutest;

public class Label {

    private String text;
    private int fontSize;

    public Label(String text, int fontSize) {
        this.text = text;
        this.fontSize = fontSize;
    }

    public void setText(char[] charArray) {
        text = "";
        for (int i = 0; i < charArray.length; i++) {
            text += charArray[i];
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getFontSize() {
        return fontSize;
    }
}
