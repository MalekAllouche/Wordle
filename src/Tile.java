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
//        this.setFont(new Font("Helvetica", Font.BOLD, 30));
        //The background is set to white
        this.setBackground(new Color(255,255,255));
        //The foreground is set to black
        this.setForeground(new Color(0,0,0));
        //This is used when letters are grayed out
        this.setDisabledTextColor(new Color(255));
        //Aligning it to the center
        this.setHorizontalAlignment(CENTER);

    }

}
