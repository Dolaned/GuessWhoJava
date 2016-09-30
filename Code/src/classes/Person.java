package classes;

import classes.AttributePair;

import java.util.ArrayList;

/**
 * Created by dylanaird on 24/09/2016.
 */
public class Person {

    private String playerName;
    private ArrayList<AttributePair> pairs;

    public Person(String playerName){
        this.playerName = playerName;
        pairs = new ArrayList<>();
    }

    public Boolean hasAttriutePair(AttributePair pair){

        for (AttributePair pair2 : this.pairs) {
            if (pair2.getAttribute().equals(pair.getAttribute())) {
                if (pair2.getValue().equals(pair.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }


    public void addattributePair(AttributePair pair){
        this.pairs.add(pair);
    }

    public ArrayList<AttributePair> getPairs() {
        return pairs;
    }

    public void setPairs(ArrayList<AttributePair> pairs) {
        this.pairs = pairs;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
