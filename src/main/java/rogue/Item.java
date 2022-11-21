package rogue;
import java.awt.Point;
import java.io.Serializable;

/**
 * A basic Item class; basic functionality for both consumables and equipment.
 */
public class Item implements Serializable {
    private int itemId;
    private String itemName;
    private String itemType;
    private Point itemXyLocation;
    private Character displayCharacter;
    private String description;
    private Room currentRoom;
    private String symbol;


    //Constructors
    /**
     * Default constructor for Item class.
     */
    public Item() {
        itemId = 0;
        itemName = " ";
        itemType = " ";
        itemXyLocation = new Point(0, 0);
        displayCharacter = 'x';
        description = " ";
        symbol = " ";
    }

    /**
     * Secondary constructor for Item class containing many passed params.
     * @param id the item id
     * @param name the name of the item
     * @param type the type of item (ie. consumable)
     * @param xyLocation the x,y coordinate position of item on game board
     */
    public Item(int id, String name, String type, Point xyLocation) {
        itemId = id;
        itemName = name;
        itemType = type;
        itemXyLocation = xyLocation;
    }


    // Getters and setters

    /**
     * Getter for id of item.
     * @return the int id of the selected item
     */
    public int getId() {
        return itemId;
    }

    /**
     * Setter for id of item from passed id param.
     * @param id sets id of selected item
     */
    public void setId(int id) {
        itemId = id;
    }

    /**
     * Getter for name of item.
     * @return the String name of item
     */
    public String getName() {
        return itemName;
    }

    /**
     * Setter for name of item from passed name param.
     * @param name sets name of selected item
     */
    public void setName(String name) {
        itemName = name;
    }

    /**
     * Getter for type of item.
     * @return the String type of the selected item
     */
    public String getType() {
        return itemType;
    }

    /**
     * Setter for type of item from passed type param.
     * @param type sets type of selected item
     */
    public void setType(String type) {
        itemType = type;
    }

    /**
     * Getter for displaycharacter of item.
     * @return the Character stored in displaycharacter
     */
    public Character getDisplayCharacter() {
        return displayCharacter;
    }

    /**
     * Setter for display character of item.
     * @param newDisplayCharacter Character value passed and set
     */
    public void setDisplayCharacter(Character newDisplayCharacter) {
        displayCharacter = newDisplayCharacter;
    }

    /**
     * Getter for description of item.
     * @return String description of item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description of item from passed param.
     * @param newDescription String value passed to description
     */
    public void setDescription(String newDescription) {
     description = newDescription;
    }

    /**
     * Getter for xy coordinate of item.
     * @return Point storing xy coord of item
     */
    public Point getXyLocation() {
        return itemXyLocation;
    }

    /**
     * Setter for xy coordinate of item.
     * @param newXyLocation Point to be passed to itemxylocation
     */
    public void setXyLocation(Point newXyLocation) {
        itemXyLocation = newXyLocation;
    }

    /**
     * Getter for current room the item is in.
     * @return the room which the item is in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Setter for current room of item.
     * @param newCurrentRoom Passed room the item is in.
     */
    public void setCurrentRoom(Room newCurrentRoom) {
        currentRoom = newCurrentRoom;
    }

    /**
     * Getter for item symbol that represents it on the game board.
     * @return the symbol of the selected item
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Setter for symbol of item.
     * @param symb the String representation of symbol.
     */
    public void setSymbol(String symb) {
        symbol = symb;
    }


}
