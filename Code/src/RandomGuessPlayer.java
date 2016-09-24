import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * Random guessing player.
 * This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player
{
    //collection of people to guess from
    HashMap<String, Person> peopleMap = new HashMap<>();

    //collection of attribute pairs the above might have
    ArrayList<AttributePair> attributePairs = new ArrayList<>();
    //current lpayer selected
    Person currentPlayer;
    //random class to randomly pick an attribute.
    private Random randomGenerator = new Random();

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName Name of the chosen person for this player.
     * @throws IOException If there are IO issues with loading of gameFilename.
     *    Note you can handle IOException within the constructor and remove
     *    the "throws IOException" method specification, but make sure your
     *    implementation exits gracefully if an IOException is thrown.
     */
    public RandomGuessPlayer(String gameFilename, String chosenName)
    {
        int lineCounter = 0;
        try {
            BufferedReader assignedReader = new BufferedReader(new FileReader(gameFilename));
            String line = null;
            while ((line = assignedReader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        //assign the current player to one of the inputted players.
        this.currentPlayer = peopleMap.get(chosenName);
    } // end of RandomGuessPlayer()


    public Guess guess() {

        if(this.peopleMap.size() > 1){

            //pick random attribute from attribute list.
            int index = this.randomGenerator.nextInt(this.attributePairs.size());
            AttributePair pair = this.attributePairs.get(index);

            //remove it from list and ask it as a guess.
            this.attributePairs.remove(index);
            return new Guess(Guess.GuessType.Attribute, pair.getAttribute(), pair.getValue());

        }else{
            return new Guess(Guess.GuessType.Person, "", peopleMap.entrySet().iterator().next().getValue().getPlayerName());
        }
    } // end of guess()


    public boolean answer(Guess currGuess) {

        ArrayList<AttributePair> currentAttr = this.currentPlayer.getPairs();

        for(AttributePair pair: currentAttr){
            if(Objects.equals(pair.getAttribute(), currGuess.getAttribute())){
                if(Objects.equals(pair.getValue(), currGuess.getValue())){
                    return true;
                }
            }
        }
        // placeholder, replace
        return false;
    } // end of answer()


	public boolean receiveAnswer(Guess currGuess, boolean answer) {



        //receive guess and answer
        //for each player in player map, check current guess attribute and remove from person queue


        // placeholder, replace
        return true;
    } // end of receiveAnswer()

} // end of class RandomGuessPlayer
