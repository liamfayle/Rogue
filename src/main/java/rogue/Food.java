package rogue;


public class Food extends Item implements Edible {

    /**
     * Default constructor.
     */
    public Food() {

    }


    /**
     * Secondary constructor for food item.
     * @param i item to be added to food
     */
    public Food(Item i) {
        setId(i.getId());
        setDescription(i.getDescription());
        setName(i.getName());
        setType(i.getType());
    }



    /**
     * Eat method.
     * @return eat string
     */
    public String eat() {
        return getDescription() + "!";
    }

}
