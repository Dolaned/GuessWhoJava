package def;

import classes.AttributePair;
import classes.FileHandler;
import classes.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the def.Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {

    //collection of people to guess from
    private HashMap<String, Person> peopleMap = new HashMap<>();
    private HashMap<AttributePair, Integer> pairCount = new HashMap<>();

    //collection of attribute pairs the above might have
    private ArrayList<AttributePair> attributePairs = new ArrayList<>();
    //current lpayer selected
    private Person currentPlayer;

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName   Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *                     Note you can handle IOException within the constructor and remove
     *                     the "throws IOException" method specification, but make sure your
     *                     implementation exits gracefully if an IOException is thrown.
     */
    public BinaryGuessPlayer(String gameFilename, String chosenName) {
        FileHandler fileHandler = new FileHandler();

        try {
            fileHandler.parseFile(gameFilename);
            this.peopleMap = fileHandler.getPeopleMap();
            this.attributePairs = fileHandler.getAttributePairs();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //assign the current player to one of the inputted players.
        this.currentPlayer = peopleMap.get(chosenName);


        for (Map.Entry<String, Person> personEntry : peopleMap.entrySet()) {

            for (AttributePair pair : personEntry.getValue().getPairs()) {
                if (pairCount.get(pair) != null) {
                    int counter = pairCount.get(pair);
                    counter++;
                    pairCount.put(pair, counter);

                } else {
                    pairCount.put(pair, 1);
                }
            }
        }


    } // end of BinaryGuessPlayer()


    public Guess guess() {

        // placeholder, replace
        return new Guess(Guess.GuessType.Person, "", "Placeholder");
    } // end of guess()


    public boolean answer(Guess currGuess) {
        Boolean retValue = false;

        if (currGuess.getType() == Guess.GuessType.Person) {
            if (currGuess.getValue().equals(this.currentPlayer.getPlayerName())) {
                retValue = true;
            }
        } else {
            retValue = currentPlayer.hasAttriutePair(new AttributePair(currGuess.getAttribute(), currGuess.getValue()));
        }
        return retValue;
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        // placeholder, replace
        return true;
    } // end of receiveAnswer()

} // end of class BinaryGuessPlayer
