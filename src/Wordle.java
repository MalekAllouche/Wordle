import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

public class Wordle extends JFrame {
    private final String word; //The word generated
    private final Tile[][] letters;//Each letter guess
    private final Button[] check;//The check button
    private Tile focusTile;//The tile the user is currently on

    public Wordle(String word) {
        //Outputting the word onto the terminal
        System.out.println("The random word is: " + word);

        //Initialising the word to be equal to the word provided in the Constructor
        this.word = word;
        //6 Guesses in total with 5 letters in each of them
        letters = new Tile[6][5];
        //The check button
        check = new Button[6];

        this.setLayout(new GridLayout(6, 6));
        this.setSize(500, 500);
        this.setTitle("Wordle!");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        //Adding tiles and buttons
        for (int i = 0; i < 6; i++) {
            for(int j = 0; j<5; j++){
                Tile letter = new Tile(i, j);

                //Adding event Listeners for each tile.
                letter.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        Runnable r = () -> {
                            if(letter.getText().length() > 0) {
                                //Setting all the characters to uppercase so the word isn't case-sensitive
                                if(Character.isLowerCase(letter.getText().charAt(0))) {
                                    letter.setText(letter.getText().toUpperCase());
                                }
                                if(letter.getLetterNumber()<letters[letter.getLetterNumber()].length-1){
                                    SwingUtilities.invokeLater(()->letters[letter.getGuess()][letter.getLetterNumber()+1].requestFocus());
                                    focusTile = letters[letter.getGuess()][letter.getLetterNumber()+1];
                                }
                            }

                            //If the letter is not a letter, we remove the letter
                            if(letter.getText().matches("[^A-Za-z]")) {
                                letter.setText(letter.getText().replaceAll("[^A-Za-z]", ""));
                            }
                            //If more than two letters are inputted into 1 tile, we remove the letters
                            if(letter.getText().length() > 1){
                                letter.setText(letter.getText().substring(1));
                            }
                        };
                        SwingUtilities.invokeLater(r);
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {

                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {

                    }
                });

                //When user presses the backspace button
                letter.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "backspaceAction");
                letter.getActionMap().put("backspaceAction", new BackspaceAction());

                //When user presses the enter button
                letter.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
                letter.getActionMap().put("enterAction", new EnterAction());

                this.add(letter);
                letters[i][j] = letter;
                if(i>0){
                    letter.setEnabled(false);
                }
            }

            //Adding the check buttons
            Button checkButton = new Button(i);
            checkButton.addMouseListener(new MouseAdapter() {
                boolean mousePressed = false;
                /**
                 * @param e the event to be processed
                 */
                @Override
                public void mousePressed(MouseEvent e) {
//                    super.mousePressed(e);
                    mousePressed=true;
                }

                /**
                 * @param e the event to be processed
                 */
                @Override
                public void mouseReleased(MouseEvent e) {
//                    super.mouseReleased(e);
                    if(mousePressed){
                        if(SwingUtilities.isLeftMouseButton(e)){
                            onClick(checkButton);
                        }
                    }
                    mousePressed = false;
                }

                /**
                 * @param e the event to be processed
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
//                    super.mouseEntered(e);
                    mousePressed=true;
                }

                /**
                 * @param e the event to be processed
                 */
                @Override
                public void mouseExited(MouseEvent e) {
//                    super.mouseExited(e);
                    mousePressed=false;
                }
            });

            if(i>0){
                checkButton.setEnabled(false);
            }
            check[i] = checkButton;
            this.add(checkButton);
        }

        focusTile=letters[0][0];
        SwingUtilities.invokeLater(() -> focusTile.requestFocus());
    }

    private class BackspaceAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!focusTile.getText().isEmpty() && focusTile.isEnabled()){
                focusTile.setText("");
            } else if(focusTile.getLetterNumber()>0){
                focusTile = letters[focusTile.getGuess()][focusTile.getLetterNumber()-1];
                SwingUtilities.invokeLater(() -> focusTile.requestFocus());
            }
        }
    }

    private class EnterAction extends AbstractAction{
        @Override
        public void actionPerformed(ActionEvent e){
            onClick(check[focusTile.getGuess()]);
        }
    }

    //Whenever a player clicks on the check button
    private void onClick(Button button){
        Tile[] letter = letters[button.getGuessNumber()];
        StringBuilder builder = new StringBuilder();

        //Make a word out of the letters in the tiles
        for (Tile t: letter) {
            if(!t.getText().isEmpty()){
                builder.append(t.getText());
                continue;
            }
            return;
        }

        //If the word isn't a valid word, we output an error
        if(!validWord(builder.toString())) {
            JOptionPane.showMessageDialog(null, "Invalid Word! Try again");
            return;
        }

        Colours[] guessValidity = validGuess(builder.toString(), word);

        int count = 0; //Keeps track of the number of valid letters in the guessed word

        //Then, we set colours for each letter according to its validity
        for(int i = 0; i<letter.length; i++){
            if(guessValidity[i].equals(Colours.RIGHT)){
                count++;
            }
            Tile t = letters[button.getGuessNumber()][i];
            t.setBackground(guessValidity[i].colour);
            t.setEnabled(false);
            t.setEditable(false);
        }
        button.setEnabled(false);

        //When player wins,
        if(count == word.length()){
            JOptionPane.showMessageDialog(null, "You guessed the word!");
        }

        if(button.getGuessNumber() +1>word.length()){
            JOptionPane.showMessageDialog(null, "You lost! The word was "+ word +". Better luck next time!");
        }

        for(int i =0; i<letter.length; i++){
            letters[button.getGuessNumber()+1][i].setEnabled(true);
            check[button.getGuessNumber()+1].setEnabled(true);
        }
        SwingUtilities.invokeLater(()->letters[button.getGuessNumber()+1][0].requestFocus());
    }

    private static Colours[] validGuess(String guess, String word) {
        Colours[] letterStatus = new Colours[guess.length()];
        //Loop over each letter and set colours according to the validity of them
        for(int i = 0; i<guess.length(); i++){
            String guessLetter = String.valueOf(guess.charAt(i)).toLowerCase(Locale.ROOT);
            String validLetter = String.valueOf(word.charAt(i)).toLowerCase(Locale.ROOT);

            if(guessLetter.equals(validLetter)){
                letterStatus[i] = Colours.RIGHT;
            } else if(word.contains(guessLetter)){
                letterStatus[i] = Colours.WRONG;
            } else {
                letterStatus[i] = Colours.INCORRECT;
            }
        }
        return letterStatus;
    }

    private static boolean validWord(String word){
        return Main.dictionary.contains(word.toLowerCase(Locale.ROOT));
    }
}