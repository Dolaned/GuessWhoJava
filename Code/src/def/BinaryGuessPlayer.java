package def;

import classes.AttributePair;
import classes.FileHandler;
import classes.Person;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the def.Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {

    //collection of people to guess from
    private ConcurrentHashMap<String, Person> peopleMap = new ConcurrentHashMap<>();
    private Integer[] pairCount;

    private ArrayList<AttributePair> guessed = new ArrayList<>();

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

    } // end of BinaryGuessPlayer()


    public void setupArray(){

        this.pairCount = new Integer[this.attributePairs.size()];
        // Sorting the arraylist.

        for (int i = 0; i < this.attributePairs.size(); i++) {
            this.pairCount[i] = this.attributePairs.get(i).getOccurence();
        }
    }


    public Guess guess() {
        //the meat and potatoes of the algorithm
        //the guess function is broken up into to parts
        //Part 1 -- Iterate through the remaining people in the set and grab all there attribute
        //       -- Keep a tally of these attributes and how many times they occur
        //
        //Part 2 -- Go through the map of viable attribute guesses to find the best guess
        //       -- the "best guess" is an attribute that occurs in half of the remaining people
        //       -- If there isn't an attribute that occurs exactly that amount, select the one that is the closest
        HashMap<String, Integer> tempAP = new HashMap<>();
        Boolean invalidQuestion; 

        String keyString;

        int counter;

        //my turn for a TRIPLE NESTED FOR LOOP; BOO-YAH!
        //PART 1 -- building <attribute, occurance> map
        for (Map.Entry<String, Person> personEntry : peopleMap.entrySet()) 
        {
            for (AttributePair p : personEntry.getValue().getPairs()) 
            {
                invalidQuestion = false;
                for(AttributePair p2 : guessed)
                {
                    if((p.getAttribute().equals(p2.getAttribute())) && (p.getValue().equals(p2.getValue())))
                    {
                        invalidQuestion = true;
                    }
                }
                
                if(!invalidQuestion)
                {
                    keyString = p.getAttribute();
                    keyString = keyString.concat(" ");
                    keyString = keyString.concat(p.getValue());
                    if(tempAP.get(keyString) == null)
                    {
                        counter = 0;
                    }
                    else
                    {
                        counter = tempAP.get(keyString);
                    }
                    counter = counter + 1;
                    tempAP.put(keyString, counter);
                }
            }
        }
        // Find the ideal guess amount an initailise the best guess
        int idealGuess = peopleMap.size()/2;
        AttributePair bestGuess = null;
        String apValue, apAttribute;

        //PART 2 -- find the attribute that represents the best guess to cut the number of remaining people
        //       -- if there is only 1 person remaining in the peopleMap, guess that person
        if(peopleMap.size() > 1)
        {
            for(Map.Entry<String, Integer> ap: tempAP.entrySet())
            {
                String[] apResult = ap.getKey().split("\\s");
                apAttribute = apResult[0];
                apValue = apResult[1];
                //System.out.println("---" +apAttribute +" "+ apValue +" "+ ap.getValue() +" "+ idealGuess);
                if(ap.getValue() == idealGuess)
                {
                    guessed.add(new AttributePair(apAttribute, apValue));
                    return new Guess(Guess.GuessType.Attribute, apAttribute, apValue);
                }
                else
                {
                    if(bestGuess== null)
                    {
                        bestGuess = new AttributePair(apAttribute, apValue);
                    }
                    else
                    {
                        String currBest = bestGuess.getAttribute();
                        currBest = currBest.concat(" ");
                        currBest = currBest.concat(bestGuess.getValue());
                        if(Math.abs(idealGuess - tempAP.get(currBest)) > Math.abs(idealGuess - ap.getValue()))
                        {
                            bestGuess = new AttributePair(apAttribute, apValue);
                        }
                    }
                 }
             }
             guessed.add(bestGuess);
             return new Guess(Guess.GuessType.Attribute, bestGuess.getAttribute(), bestGuess.getValue());
         }
         else
         {
             return new Guess(Guess.GuessType.Person, "", peopleMap.entrySet().iterator().next().getKey());
         }
        
    } // end of guess()


    public boolean answer(Guess currGuess) {
        Boolean retValue = false;
        
        //check if person guess was true or the attribute guess is true and return
        if (currGuess.getType() == Guess.GuessType.Person) 
        {
            if (currGuess.getValue().equals(this.currentPlayer.getPlayerName())) 
            {
                retValue = true;
            }
        }
        else 
        {
            retValue = currentPlayer.hasAttributePair(new AttributePair(currGuess.getAttribute(), currGuess.getValue()));
        }
        return retValue;
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        //recieving answer to update the peopleMap of remaining valid people options
        //first check if the guess is an person or attribute guess
        if(currGuess.getType() == Guess.GuessType.Person)
        {
            if (answer)
            {
                return true;
            }
            else
            {
                this.peopleMap.remove(currGuess.getValue());
            }
        }
        else
        {
            //if attribute guess iterate through the remaining peopl in the MAp
            //then check if that person either contain or didn't contain the attribute
            //and then remove or preseve depending on the answer to the guess
            for(Map.Entry<String, Person> tempPerson : this.peopleMap.entrySet())
            {
                Person p = tempPerson.getValue();
                AttributePair ap = new AttributePair(currGuess.getAttribute(), currGuess.getValue());
                if(p.hasAttributePair(ap))
                {
                    if(!answer)
                    {
                        this.peopleMap.remove(tempPerson.getKey());
                    }
                }    
                else
                {
                    if(answer)
                    {
                        this.peopleMap.remove(tempPerson.getKey());
                    }
                }
            }
        }

        return false;
    } // end of receiveAnswer()

} // end of class BinaryGuessPlayer
