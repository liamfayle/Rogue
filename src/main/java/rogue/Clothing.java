package rogue;


public class Clothing extends Item implements Wearable {

    /**
     * Default constructor.
     */
    public Clothing() {

    }

    /**
     * Secondary constructor.
     * @param i item to be added
     */
    public Clothing(Item i) {
        setId(i.getId());
        setDescription(i.getDescription());
        setName(i.getName());
        setType(i.getType());
    }

    /**
     * Wear method.
     * @return string
     */
    public String wear() {
        return getDescription();
    }

}
