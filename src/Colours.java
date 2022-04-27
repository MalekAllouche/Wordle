import java.awt.*;

/**
 * This class sets 3 different colours for each letter.
 * 1. Green: For the letter at the right spot
 * 2. Yellow: For the letter at the wrong spot
 * 3. Grey: For the letter not in the word*/
public enum Colours {
    //Setting the colours using enums
    RIGHT(new Color(0, 128, 0)), //Green
    WRONG(new Color(255, 255, 0)), //Yellow
    INCORRECT(new Color(128, 128, 128)); //Grey
    public final Color colour;
    Colours(Color colour){
        this.colour = colour;
    }
}