package def;

import classes.AttributePair;
import classes.FileHandler;
import classes.Person;

import java.io.IOException;
import java.util.*;

/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the def.Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {
    //collection of people to guess from
    private HashMap<String, Person> peopleMap = new HashMap<>();

    //collection of attribute pairs the above might have
    private ArrayList<AttributePair> attributePairs = new ArrayList<>();
    //current lpayer selected
    private Person currentPlayer;
    //random class to randomly pick an attribute.
    private Random randomGenerator = new Random();

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
    public RandomGuessPlayer(String gameFilename, String chosenName) {
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
    } // end of def.RandomGuessPlayer()

    public Guess guess() {

        if (this.peopleMap.size() > 1) {

            //pick random attribute from attribute list.
            int index = this.randomGenerator.nextInt(this.attributePairs.size());
            AttributePair pair = this.attributePairs.get(index);

            //remove it from list and ask it as a guess.
            this.attributePairs.remove(index);
            return new Guess(Guess.GuessType.Attribute, pair.getAttribute(), pair.getValue());

        } else {
            return new Guess(Guess.GuessType.Person, "", peopleMap.entrySet().iterator().next().getValue().getPlayerName());
        }
    } // end of guess()

    public boolean answer(Guess currGuess) {
        Boolean retValue =false;

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

        if (currGuess.getType() == Guess.GuessType.Person) {
            if (answer) {
                return true;
            }
        } else {
            if (answer) {
                ArrayList<String> marked = new ArrayList<>();

                for (Map.Entry<String, Person> entry : this.peopleMap.entrySet()) {

                    Person p = entry.getValue();
                    Boolean hasAttribute = false;
                    for (AttributePair pair : p.getPairs()) {

                        if (pair.getAttribute().equals(currGuess.getAttribute())) {
                            if (pair.getValue().equals(currGuess.getValue())) {
                                hasAttribute = true;
                            }
                        }
                    }
                    if (!hasAttribute) {
                        marked.add(entry.getKey());
                    }
                }
                for (String s : marked) {
                    //System.out.println("Removing : " + s);
                    this.peopleMap.remove(s);
                }

                Iterator<AttributePair> iter = this.attributePairs.iterator();

                while (iter.hasNext()) {
                    AttributePair pair = iter.next();

                    if (pair.getAttribute().equals(currGuess.getAttribute()))
                        iter.remove();

                }
            }
        }
        // placeholder, replace
        return false;
    } // end of receiveAnswer()

} // end of class def.RandomGuessPlayer
