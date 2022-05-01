import javax.swing.*;

/**
 * This is the class which makes and implements the "Check" Button
 **/
public class Button extends JButton {
    private final int row;

    //Constructor method to initialise the button
    public Button(int row){
        this.row = row;
        this.setText("Check");
    }

    public int getRow() {
        return row;
    }
}
