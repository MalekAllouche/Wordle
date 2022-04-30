import javax.swing.*;
import java.awt.*;

public class Tile extends JTextField {
    private final int guess;
    private final int tileNumber;
    public int getGuess(){
        return guess;
    }
    public int getTileNumber(){
        return tileNumber;
    }

    public Tile(int guess, int tileNumber){
        this.guess = guess;
        this.tileNumber = tileNumber;
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
