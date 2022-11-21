package rogue;

public class SmallFood extends Food implements Tossable {
    private String[] useStrings;
    private String eatString;
    private String tossString;

    /**
     * Default constructor.
     */
    public SmallFood() {

    }


    /**
     * secondary constrcutor.
     * @param i item to be added
     */
    public SmallFood(Item i) {
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
     * eat method.
     * @return string
     */
    public String eat() {
        return eatString;
    }

    /**
     * toss method.
     * @return string
     */
    public String toss() {
        return tossString;
    }

}
