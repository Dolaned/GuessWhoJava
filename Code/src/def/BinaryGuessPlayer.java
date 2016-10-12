package def;

import classes.AttributePair;
import classes.FileHandler;
import classes.Person;

import java.io.IOException;
import java.util.*;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the def.Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer  implements Player  {

    //collection of people to guess from
    private HashMap<String, Person> peopleMap = new HashMap<>();
    private Integer[] pairCount;

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

        //calculate and sort the attribute occurences per player.
        this.calcAndSortOccurences();


    } // end of BinaryGuessPlayer()

    public Guess guess() {

        //perform binary search for most optimal result
        if (peopleMap.size() > 1 && this.attributePairs.size() != 0) {
            int guess = peopleMap.size() / 2;
            AttributePair nextGuess = this.attributePairs.get(binarySearch(guess));
            return new Guess(Guess.GuessType.Attribute, nextGuess.getAttribute(), nextGuess.getValue());

        } else {//take a guess its 50/50
            return new Guess(Guess.GuessType.Person, "", peopleMap.entrySet().iterator().next().getKey());
        }
    } // end of guess()


    public boolean answer(Guess currGuess) {
        Boolean retValue = false;

        if (currGuess.getType() == Guess.GuessType.Person) {
            if (currGuess.getValue().equals(this.currentPlayer.getPlayerName())) {
                retValue = true;
            }
        } else {
            retValue = currentPlayer.hasAttributePair(new AttributePair(currGuess.getAttribute(), currGuess.getValue()));
        }
        return retValue;
    } // end of answer()


    public boolean receiveAnswer(Guess currGuess, boolean answer) {

        if (currGuess.getType() == Guess.GuessType.Person) {
            if (answer) {
                return true;
            } else {
                this.peopleMap.remove(currGuess.getValue());
            }
        } else {
            removeQuestions(currGuess, answer);
        }
        // placeholder, replace
        return false;
    } // end of receiveAnswer()


    private boolean pairsMatch(AttributePair pair1, AttributePair pair2) {
        if (pair1.getAttribute().equals(pair2.getAttribute())) {
            if (pair1.getValue().equals(pair2.getValue())) {
                return true;
            }
        }
        return false;
    }

    private int binarySearch(int key) {
        int low = 0;
        int high = this.attributePairs.size();

        while (high >= low) {

            int middle = (low + high) / 2;
            //System.out.println("Size is " + this.attributePairs.size());
            //System.out.println(low + "<low |<mid>"+middle+" <key> " +key+"|  high> " + high + " Value : " + this.attributePairs.get(middle).getOccurence() );
            if (this.attributePairs.get(middle).getOccurence() == key) {
                return middle;
            }
            if (this.attributePairs.get(middle).getOccurence() < key) {
                low = middle + 1;
            }
            if (this.attributePairs.get(middle).getOccurence() > key) {
                high = middle - 1;
            }
        }
        return -1;
    }

    private void removeQuestions(Guess guess, Boolean answer) {

        ArrayList<String> marked = new ArrayList<>();

        for (Map.Entry<String, Person> entry : this.peopleMap.entrySet()) {

            Person p = entry.getValue();
            Boolean hasAttribute = !answer;
            if (p.hasAttributePair(new AttributePair(guess.getAttribute(), guess.getValue()))) {
                hasAttribute = !hasAttribute;
            }
            if (!hasAttribute) {
                marked.add(entry.getKey());
            }
        }
        for (String s : marked) {
            this.peopleMap.remove(s);
        }

        Iterator<AttributePair> iter = this.attributePairs.iterator();
        while (iter.hasNext()) {
            AttributePair pair = iter.next();

            if (pair.getAttribute().equals(guess.getAttribute()) || pair.getOccurence() > this.peopleMap.size() / 2)
                iter.remove();
        }
    }

    private void calcAndSortOccurences() {
        // this is horrible but works.
        for (Map.Entry<String, Person> personEntry : peopleMap.entrySet()) {
            for (AttributePair attributePair : attributePairs) {
                for (AttributePair p : personEntry.getValue().getPairs()) {
                    if (pairsMatch(p, attributePair)) {
                        int occ = attributePair.getOccurence();
                        attributePair.setOccurence(occ + 1);
                    }
                }
            }
        }

        Collections.sort(attributePairs, new Comparator<AttributePair>() {
            @Override
            public int compare(AttributePair o1, AttributePair o2) {
                return o1.getOccurence().compareTo(o2.getOccurence());
            }
        });

        Iterator<AttributePair> iter = this.attributePairs.iterator();
        while (iter.hasNext()) {
            AttributePair pair = iter.next();

            if(pair.getOccurence() == 0)
                iter.remove();
        }
    }

} // end of class BinaryGuessPlayer
