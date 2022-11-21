package rogue;


public class Potion extends Magic implements Edible, Tossable {
    private String[] useStrings;
    private String eatString;
    private String tossString;

    /**
     * Default constructor for potion.
     */
    public Potion() {

    }

    /**
     * Secondary constructor.
     * @param i item to be added
     */
    public Potion(Item i) {
        setId(i.getId());
        setDescription(i.getDescription());
        setName(i.getName());
        setType(i.getType());
        setStrings();
    }

    private void setStrings() {
        useStrings = getDescription().split(":");
        if (useStrings.length == 1) {
            eatString = getDescription();
            tossString = "Dropped " + getType() + " at feet";
        } else {
            eatString = useStrings[0];
            tossString = useStrings[1];
        }
    }

    /**
     * Eat method.
     * @return string
     */
    public String eat() {
        return eatString;
    }

    /**
     * Inherited toss method.
     * @return string
     */
    public String toss() {
        return tossString;
    }

}
