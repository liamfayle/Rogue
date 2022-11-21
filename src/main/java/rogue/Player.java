package rogue;
//import java.util.ArrayList;
import java.awt.Point;
import java.io.Serializable;
/**
 * The player character.
 */
public class Player implements Serializable {
    private String playerName;
    private Point xyLocation;
    private Room currentRoom;
    private String symbol;


    /**
     * Default constructor for Player class.
     */
    public Player() {
        playerName = "Liam";
        xyLocation = new Point(1, 1);
        symbol = "@";
    }

    /**
     * Secondary constructor for player class.
     * @param name String name of player to be set
     */
    public Player(String name) {
        playerName = name;
    }

    /**
     * Getter for player name.
     * @return String name of player
     */
    public String getName() {
        return playerName;
    }

    /**
     * Setter for Player name.
     * @param newName String name of player to be set
     */
    public void setName(String newName) {
        playerName = newName;
    }

    /**
     * Getter for xy coordinate of player.
     * @return Point (x,y) where player is located
     */
    public Point getXyLocation() {
        return xyLocation;
    }

    /**
     * Setter for player's xy coordinate.
     * @param newXyLocation Point (x,y) to set players location
     */
    public void setXyLocation(Point newXyLocation) {
        xyLocation = newXyLocation;
    }

    /**
     * Getter for current room player is in.
     * @return Room the player is in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Setter for room player is in.
     * @param newRoom Room to be set as players current room.
     */
    public void setCurrentRoom(Room newRoom) {
        currentRoom = newRoom;
    }

    /**
     * Getter for String symbol of player.
     * @return String representation of player symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Setter for String symbol of player.
     * @param symb String to be set as player symbol
     */
    public void setSymbol(String symb) {
        symbol = symb;
    }
}
