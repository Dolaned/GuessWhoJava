package classes;

import java.util.UUID;

/**
 * Created by dylanaird on 24/09/2016.
 */
public class AttributePair {

    //current attribute
    private String attribute;
    //its value
    private String value;
    //how many times it occurs in the players
    private int occurence = 0;
    //the chance it will half the player map
    private int chance = 0;

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private UUID id;

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }


    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AttributePair(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
        this.id = UUID.randomUUID();
    }
}
