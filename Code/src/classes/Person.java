package classes;

import java.util.ArrayList;

/**
 * Created by dylanaird on 24/09/2016.
 */
public class Person {

    //this is the player name
    private String playerName;
    //this is the collection of its attribute pairs.
    private ArrayList<AttributePair> pairs;

    //constructor for the person
    public Person(String playerName) {
        this.playerName = playerName;
        pairs = new ArrayList<>();
    }

    //checks if the current player has given attribute.
    public Boolean hasAttributePair(AttributePair pair) {

        for (AttributePair pair2 : this.pairs) {
            if (pair2.getAttribute().equals(pair.getAttribute())) {
                if (pair2.getValue().equals(pair.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }


    //pushes attribute pair to player.
    public void addattributePair(AttributePair pair) {
        this.pairs.add(pair);
    }

    //get pair collection
    public ArrayList<AttributePair> getPairs() {
        return pairs;
    }

    //gets player name
    public String getPlayerName() {
        return playerName;
    }
}
