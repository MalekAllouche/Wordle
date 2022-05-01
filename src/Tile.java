import javax.swing.*;
import java.awt.*;

public class Tile extends JTextField {
    private final int row;
    private final int column;
    public Tile(int row, int column){
        this.row = row;
        this.column = column;
        //The background is set to white
        this.setBackground(new Color(255,255,255));
        //The foreground is set to black
        this.setForeground(new Color(0,0,0));
        //This is used when letters are grayed out
        this.setDisabledTextColor(new Color(255));
        //Aligning it to the center
        this.setHorizontalAlignment(CENTER);
    }

    public int getRow(){
        return row;
    }
    public int getColumn(){
        return column;
    }

}
