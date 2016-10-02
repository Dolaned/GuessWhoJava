package classes;

import java.util.UUID;

/**
 * Created by dylanaird on 24/09/2016.
 */
public class AttributePair {

    private String attribute;
    private String value;
    private Integer occurence =0;

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

    public AttributePair(String attribute, String value){
        this.attribute = attribute;
        this.value = value;
    }
}
