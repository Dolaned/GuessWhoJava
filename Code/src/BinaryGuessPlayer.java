import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {

    //collection of people to guess from
    private ConcurrentHashMap<String, Person> peopleMap = new ConcurrentHashMap<>();

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
        //calculate the
        this.calcAndSortProb();
        //perform binary search for most optimal result
        if (peopleMap.size() > 1) {
            //static guess to make sur the guess is in the closest region of half the people map size
            int guess = 50;

            //use get closest value to return the closet value to 50% of the array.
            AttributePair nextGuess = this.attributePairs.get(getClosestValue(guess));
            this.attributePairs.remove(getClosestValue(guess));
            return new Guess(Guess.GuessType.Attribute, nextGuess.getAttribute(), nextGuess.getValue());

        } else {//take a guess its 50/50
            return new Guess(Guess.GuessType.Person, "", peopleMap.entrySet().iterator().next().getKey());
        }
    } // end of guess()


    public boolean answer(Guess currGuess) {
        Boolean retValue = false;

        //check if person guess was true or the attribute guess is true and return
        if (currGuess.getType() == Guess.GuessType.Person) {
            if (currGuess.getValue().equals(this.currentPlayer.getPlayerName())) {
                retValue = true;
            }
        } else {
            retValue = this.currentPlayer.hasAttributePair(new AttributePair(currGuess.getAttribute(), currGuess.getValue()));
        }
        return retValue;
    } // end of answer()


    //pretty straight forward receives answer and calls removeQuestions from down below.
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
        return false;
    } // end of receiveAnswer()


    //this function just checks if two attribute pairs match.
    private boolean pairsMatch(AttributePair pair1, AttributePair pair2) {
        if (pair1.getAttribute().equals(pair2.getAttribute())) {
            if (pair1.getValue().equals(pair2.getValue())) {
                return true;
            }
        }
        return false;
    }

    //This is the heart of the program, this function searches for the value of 50
    //this value represents removing halve of the remaining people.
    //it does this by comparing the .getchance() function of the attribute pair.
    //if it cannot find 50 it returns the value of the upper and lower bounds, which
    //are the closest values to 50.
    private int[] binarySearch(int key) {
        int low = 0;
        int high = this.attributePairs.size() - 1;
        int[] lowHigh = new int[2];

        while (high >= low) {
            int middle = (low + high) / 2;

            if (this.attributePairs.get(middle).getChance() < key) {
                low = middle + 1;
            } else if (this.attributePairs.get(middle).getChance() > key) {
                high = middle - 1;
            } else {
                lowHigh[0] = middle;
                return lowHigh;
            }
        }
        lowHigh[0] = this.attributePairs.indexOf(this.attributePairs.get(high));
        lowHigh[1] = this.attributePairs.indexOf(this.attributePairs.get(low));
        return lowHigh;
    }


    private int getClosestValue(Integer search_val) {
        int low_dist, high_dist;

        // If the search value is less than or equal to the lower bound, assign the lower bound
        if (search_val <= this.attributePairs.get(0).getChance()) {
            return this.attributePairs.indexOf(this.attributePairs.get(0));
        }
        // If the search value is greater than or equal to the upper bound, assign the upper bound
        else if (search_val >= this.attributePairs.get(this.attributePairs.size() - 1).getChance()) {
            return this.attributePairs.indexOf(this.attributePairs.get(this.attributePairs.size() - 1));
        }

        // Find the bisection of the sub-tree for the search value (low and high)
        int[] position = this.binarySearch(search_val);

        // We found the value in the tree
        if (position[1] == 0) {
            if (this.attributePairs.get(position[0]).getChance() == search_val) {
                return position[0];
            }
        }

        // Calculate the distance between the upper and lower bound of the bisection
        high_dist = position[0] - search_val;
        low_dist = search_val - position[1];

        if (high_dist > low_dist) {
            return this.attributePairs.indexOf(this.attributePairs.get(position[0]));
        } else {
            return this.attributePairs.indexOf(this.attributePairs.get(position[1]));
        }
    }

    //this function removes the questions given whether the response from the opposing player was true or false;
    private void removeQuestions(Guess guess, Boolean answer) {

        for (Map.Entry<String, Person> entry : this.peopleMap.entrySet()) {

            if (entry.getValue().hasAttributePair(new AttributePair(guess.getAttribute(), guess.getValue()))) {

                if (!answer) {
                    this.peopleMap.remove(entry.getKey());
                }
            } else {
                if (answer) {
                    this.peopleMap.remove(entry.getKey());
                }
            }
        }

        //this removes all types of the attribute questions if a true is received. ie hat 2 = true, remove all hats.
        if (answer) {
            Iterator<AttributePair> iter = this.attributePairs.iterator();
            while (iter.hasNext()) {
                AttributePair pair = iter.next();
                if (Objects.equals(pair.getAttribute(), guess.getAttribute())) {
                    iter.remove();
                }
            }
        }
    }

    //this function calculates how many times each attribute pair happens in the inputted people map data set.
    //once it has done this it turns them to a % of 100, this is what helps the binary search about do the split
    // as it splits on % close to 50.
    private void calcAndSortOccurences() {
        // this is horrible but works.
        for (Map.Entry<String, Person> personEntry : peopleMap.entrySet()) {
            for (AttributePair attributePair : this.attributePairs) {
                for (AttributePair p : personEntry.getValue().getPairs()) {
                    if (pairsMatch(p, attributePair)) {
                        int occ = attributePair.getOccurence();
                        attributePair.setOccurence(occ + 1);
                    }
                }
            }
        }

        //iterates over the attribute pairs and removes values of 0 or 100 as these will produce in accurate searching.
        Iterator<AttributePair> iter = this.attributePairs.iterator();
        while (iter.hasNext()) {
            AttributePair pair = iter.next();
            double p = pair.getOccurence();
            double c = p / peopleMap.size() * 100;
            pair.setChance((int)c);
            if (pair.getChance() == 100 || pair.getChance() == 0) {
                iter.remove();
            }
        }

        //finally sort the array, this step is needed to perform a proper binary search, as they only work effectively
        //on sorted arrays.
        Collections.sort(this.attributePairs, new Comparator<AttributePair>() {
            @Override
            public int compare(AttributePair o1, AttributePair o2) {
                return o1.getOccurence().compareTo(o2.getOccurence());
            }
        });
    }
    //this function is called after every round to recalculate the occurences in the remaining pairs, not eliminated from
    //previous rounds.
    private void calcAndSortProb() {
        for (AttributePair pair : this.attributePairs) {
            pair.setOccurence(0);
            pair.setChance(0);
        }
        this.calcAndSortOccurences();
    }

} // end of class BinaryGuessPlayer
