package classes;

/**
 * Created by dylanaird on 24/09/2016.
 */
public class AttributePair {

    private String attribute;
    private String value;

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
