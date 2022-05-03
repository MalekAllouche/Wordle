import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Locale;

public class Wordle extends JFrame {
    //The word generated
    private final String word;
    //Each letter's tile. 6 rows and 5 columns
    private final Tile[][] tiles = new Tile[6][5];
    //The check button
    private final Button[] checkButtons = new Button[6];
    //The tile the user is currently on. This was made to make sure we know where the user is
    //to make this app just like the original wordle
    private Tile currentTile;

    /**
     * This is the constructor function which is the main code for the game
     * @param word The word which is to be guessed*/
    public Wordle(String word) {
        //Initialising the word to be equal to the word provided in the Constructor
        this.word = word;
        //Setting the default size of the frame
        this.setSize(550, 550);
        //6 rows and 6 columns.
        this.setLayout(new GridLayout(6, 6));
        //Setting the title
        this.setTitle("Wordle!");
        //So that the programme stops when the app is closed
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        //Making the Frame visible
        this.setVisible(true);

        //Adding tiles to the frame where user inputs letters
        for (int i = 0; i < 6; i++) {
            for(int j = 0; j < 5; j++) {
                //Tiles are added for 6 rows and 5 columns
                Tile tile = new Tile(i, j);
                //Adding event Listeners for each tile. This makes it easier to insert letters and
                //delete them like in the original wordle
                tile.getDocument().addDocumentListener(new DocumentListener() {
                    //This method runs whenever there was an insert into the game
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        //Initialising a runnable every time there was an insert to the game
                        Runnable r = () -> {
                            //If anything was entered into the tile,
                            if(tile.getText().length() > 0) {
                                //We set all the characters to uppercase so the word isn't case-sensitive anymore
                                if(Character.isLowerCase(tile.getText().charAt(0))) {
                                    tile.setText(tile.getText().toUpperCase());
                                }
                                //If we haven't reached the end of the word, we keep on changing the focus to
                                //the next tile to help user enter their word without manually changing the tile each
                                //time. This also only happens if the input is a valid letter
                                if(tile.getColumn()<4 && !tile.getText().matches("[^A-Za-z]")) {
                                    //We then change the current tile to the next tile for seamlessly helping user
                                    //change their tiles
                                    currentTile = tiles[tile.getRow()][tile.getColumn()+1];
                                    SwingUtilities.invokeLater(()->tiles[tile.getRow()][tile.getColumn()+1].requestFocus());
                                }
                            }

                            //If the letter is not a letter, we remove the letter
                            if(tile.getText().matches("[^A-Za-z]")) {
                                tile.setText(tile.getText().replaceAll("[^A-Za-z]", ""));
                                JOptionPane.showMessageDialog(null,
                                        "Only letters are allowed as input. Please enter a valid input");
                            }
                            //If more than one letters are inputted into 1 tile, we remove the letters and only keep
                            //the new letter inputted
                            if(tile.getText().length() > 1) {
                                tile.setText(tile.getText().substring(1));
                            }
                        };
                        //Calling the runnable
                        SwingUtilities.invokeLater(r);
                    }
                    //The methods defined below are part of the DocumentListener interface but as I have no use for them,
                    //they are kept empty
                    @Override
                    public void removeUpdate(DocumentEvent e) {}
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
                });

                //When user presses the backspace button, we run the delete button action.
                //Adding a binding for the Back_Space keyStroke to the deleteButton actionMapKey.
                // This is added to each tile and implemented in the next line
                tile.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "deleteButton");
                //Adds a binding for key to action. DeleteButton method is called here which is implemented below
                tile.getActionMap().put("deleteButton", new DeleteButton());

                //When user presses the return button, we run the return button action
                //Adding a binding for keyStroke Enter to an actionMapKey which is used in the next line
                tile.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "returnButton");
                //Adds a binding for returnButton key to implement the ReturnButton method.
                //ReturnButton method is called here which is implemented below
                tile.getActionMap().put("returnButton", new ReturnButton());

                //Adding tiles into the frame
                this.add(tile);
                tiles[i][j] = tile;
                //Disabling all the tiles below the first row at the beginning so that the user can't edit tiles
                //of the rows they are not on
                if(i>0){
                    tile.setEnabled(false);
                }
            }

            //Adding the check buttons for each row
            Button checkButton = new Button(i);
            //Adding MouseListener to the check buttons to make sure we run the code when the
            //check button is clicked
            checkButton.addMouseListener(new MouseAdapter() {
                //Boolean to check when the mouse is clicked
                boolean buttonClicked = false;

                /**
                 * This method is run when the mouse is being entered on the check button
                 * @param e the event to be processed
                 */
                @Override
                public void mouseEntered(MouseEvent e) {
                    //When the mouse is entered where the button is, we set the boolean to true
                    buttonClicked=true;
                }

                /**
                 * This method is run when the mouse is being exited from the check button
                 * @param e the event to be processed
                 */
                @Override
                public void mouseExited(MouseEvent e) {
                    //When the mouse is exited from there, we set the boolean to false
                    buttonClicked=false;
                }

                /**
                 * This method is run when the mouse has been released on the check button
                 * @param e the event to be processed
                 */
                @Override
                public void mouseReleased(MouseEvent e) {
                    //If button is clicked, we check the word guessed
                    if(buttonClicked){
                        checkWord(checkButton);
                    }
                    //And then set the buttonClicked boolean to false
                    buttonClicked = false;
                }
            });

            //Disabling all buttons except for the one we are currently on
            if(i>0) {
                checkButton.setEnabled(false);
            }
            //Adding the buttons onto the frame
            checkButtons[i] = checkButton;
            this.add(checkButton);
        }
        //Setting our current tile to the first tile where the user starts typing
        currentTile=tiles[0][0];
        SwingUtilities.invokeLater(() -> currentTile.requestFocus());
    }

    private class DeleteButton extends AbstractAction {
        /**
         * This method is run each time the delete button is pressed
         * @param e is the action event each time the delete button is pressed
         * */
        @Override
        public void actionPerformed(ActionEvent e) {
            //If the current tile has a letter in it, and we press delete, the letter on the tile is removed.
            if(!currentTile.getText().isEmpty() && currentTile.isEnabled()) {
                currentTile.setText("");
            } else if(currentTile.getColumn()>0) {
                //Else if, the current tile is empty, we shift the focus to the tile before it and delete
                //the letter in the previous tile
                currentTile = tiles[currentTile.getRow()][currentTile.getColumn()-1];
                currentTile.setText("");
                SwingUtilities.invokeLater(() -> currentTile.requestFocus());
            }
        }
    }

    private class ReturnButton extends AbstractAction {
        /**
         * This method is used whenever the return button is pressed
         * @param e The action event each time the return button is pressed*/
        @Override
        public void actionPerformed(ActionEvent e) {
            //When the return button is pressed, we check the word that was input into the tiles.
            checkWord(checkButtons[currentTile.getRow()]);
        }
    }

    private static Colours[] validGuess(String guess, String word) {
        Colours[] letterStatus = new Colours[5];
        //Loop over each letter and set colours according to the validity of them
        for(int i = 0; i<5; i++) {
            //The strings below stores the characters of the actual word's letter and the guessed word's letter
            String guessLetter = String.valueOf(guess.charAt(i)).toLowerCase(Locale.ROOT);
            String validLetter = String.valueOf(word.charAt(i)).toLowerCase(Locale.ROOT);

            //Then, the colours are output on the user's device based on their guesses
            if(guessLetter.equals(validLetter)) {
                letterStatus[i] = Colours.RIGHT;
            } else if(word.contains(guessLetter)) {
                letterStatus[i] = Colours.WRONG;
            } else {
                letterStatus[i] = Colours.INCORRECT;
            }
        }
        return letterStatus;
    }

    //Whenever a player clicks on the check button or presses the return key, this method is run
    private void checkWord(Button button) {
        //This array stores the letters of the guessed word
        Tile[] letters = tiles[button.getRow()];

        //Using a string builder to make a string from the letters
        StringBuilder guessedWord = new StringBuilder();

        //Keeps track of the number of correct letters in the guessed word
        int numOfCorrectLetters = 0;

        //Making a word out of the letters in the tiles
        for (Tile t: letters) {
            if(!t.getText().isEmpty()) {
                guessedWord.append(t.getText());
                continue;
            }
            return;
        }

        //Used to output colours onto the tiles for each valid guessed letter
        Colours[] guessValidity = validGuess(guessedWord.toString(), word);

        //If the word isn't a valid word, we output an error
        if(!Main.dictionary.contains(guessedWord.toString().toLowerCase(Locale.ROOT))) {
            JOptionPane.showMessageDialog(null, "Invalid Word! Try again");
            return;
        }

        //Then, we set colours for each letter according to its validity
        for(int i = 0; i<letters.length; i++) {
            //If the letter guessed was right, we increment the number of correct letters
            if(guessValidity[i].equals(Colours.RIGHT)) {
                numOfCorrectLetters++;
            }
            //We then get the current row we are on and set the background of the letters according
            //to the guessed word. We also stop the user from editing current letters and buttons.
            Tile tile = tiles[button.getRow()][i];
            tile.setBackground(guessValidity[i].colour);
            tile.setEditable(false);
            tile.setEnabled(false);
            button.setEnabled(false);
        }

        //When numOfCorrectLetters are 5, that means the player has won. We stop the game and output the message
        if(numOfCorrectLetters == 5) {
            JOptionPane.showMessageDialog(null, "You guessed the word!");
            this.setEnabled(false);
        }

        //When the last check button is pressed and the user hasn't won, it means they've lost.
        //We then output the word as well as the message
        if(button.getRow()+1>5) {
            JOptionPane.showMessageDialog(null, "You lost! The word was "+ word +". Better luck next time!");
            this.setEnabled(false);
            return;
        }

        //When the user has gone through one guess, and got it wrong, we enable the next line of button and tiles
        for(int i =0; i<letters.length; i++) {
            tiles[button.getRow()+1][i].setEnabled(true);
            checkButtons[button.getRow()+1].setEnabled(true);
        }
        SwingUtilities.invokeLater(()->tiles[button.getRow()+1][0].requestFocus());
    }
}
