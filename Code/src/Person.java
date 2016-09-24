import java.util.ArrayList;
import java.util.HashMap;

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
