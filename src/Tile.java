import javax.swing.*;
import java.awt.*;

public class Tile extends JTextField {
    private final int guess;
    private final int letterNumber;
    public int getGuess(){
        return guess;
    }
    public int getLetterNumber(){
        return letterNumber;
    }

    public Tile(int guess, int letterNumber){
        this.guess = guess;
        this.letterNumber = letterNumber;
        //TODO: Add a font here:
        //this.setFont(new Font());
        this.setHorizontalAlignment(CENTER);
        this.setBackground(new Color(0));
        this.setForeground(new Color(255));
        this.setDisabledTextColor(new Color(255));
    }

}
