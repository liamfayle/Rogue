package rogue;


public class Ring extends Magic implements Wearable {

    /**
     * Default constructor.
     */
    public Ring() {

    }

    /**
     * Secondary constructor.
     * @param i item to be added
     */
    public Ring(Item i) {
        setId(i.getId());
        setDescription(i.getDescription());
        setName(i.getName());
        setType(i.getType());
    }


    /**
     * wear string.
     * @return wear string
     */
    public String wear() {
        return getDescription();
    }

}
