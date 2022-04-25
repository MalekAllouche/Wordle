import javax.swing.*;

/**
 * This is the class which makes and implements the "Check" Button*/
public class Button extends JButton {
    private final int guessNumber;

    //Constructor method to initialise the button
    public Button(int guessNumber){
        this.guessNumber = guessNumber;
        this.setText("Check");
    }

    public int getGuessNumber() {
        return guessNumber;
    }
}
