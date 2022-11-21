package rogue;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.Point;
import java.io.Serializable;



/**
 * A room within the dungeon - contains monsters, treasure,
 * doors out, etc.
 */
public class Room implements Serializable {
  private int width;
  private int height;
  private int id;
  private ArrayList<Item> roomItems;
  private Player player;
  private boolean inRoom;
  private HashMap<String, Integer> door;
  private HashMap<Integer, String> doorAttached;
  private HashMap<String, Door> doorArray;
  private String nsWall;
  private String ewWall;
  private String floor;
  private String doorSymbol;
  private String roomString = "";
  private ArrayList<Item> itemIds;
  private ArrayList<Room> allRooms;

  /**
   * Default room constructor.
   */
  public Room() {
    width = 0;
    height = 0;
    id = 0;
    roomItems = new ArrayList<>();
    inRoom = false;
    door = new HashMap<String, Integer>();
    doorAttached = new HashMap<Integer, String>();
    doorArray = new HashMap<String, Door>();
    nsWall = "_";
    ewWall = "|";
    doorSymbol = "+";
    allRooms = new ArrayList<>();
    itemIds = new ArrayList<>();
  }




  // Required getter and setters below

  /**
   * Getter for width of room.
   * @return int width of room
   */
  public int getWidth() {
    return width;
  }

  /**
   * Setter for width of room.
   * @param newWidth int width to be set
   */
  public void setWidth(int newWidth) {
    width = newWidth;
  }

  /**
   * Getter for heigh of room.
   * @return int height of room
   */
  public int getHeight() {
    return height;
  }

  /**
   * Setter for height of room.
   * @param newHeight int height to be set
   */
  public void setHeight(int newHeight) {
    height = newHeight;
  }

  /**
   * setup game with this add method.
   * @param item
   */
  public void addItemInit(Item item) {
    roomItems.add(item);
  }

  /**
   * Getter for id of room.
   * @return int id of room
   */
  public int getId() {
    return id;
  }

  /**
   * Setter for id of room.
   * @param newId int id of room to be set
   */
  public void setId(int newId) {
    id = newId;
  }

  /**
   * Getter for array of room items.
   * @return ArrayList of items found in selected room
   */
  public ArrayList<Item> getRoomItems() {
    return roomItems;
  }

  /**
   * Setter for room items.
   * @param newRoomItems ArrayList to be set as room items
   */
  public void setRoomItems(ArrayList<Item> newRoomItems) {
    roomItems = newRoomItems;
  }

  /**
   * Getter for player instance.
   * @return player instance
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Setter for player instance.
   * @param newPlayer Player instance to be passed and set
   */
  public void setPlayer(Player newPlayer) {
    player = newPlayer;
    inRoom = true;
  }

  /**
   * Setter for door in room.
   * @param direction String NSEW position of door (Which wall)
   * @param location int position of door in said wall
   * @param attached door class attached to current door
   * @param attachedId room id for attached door
   */
  public void setDoor(String direction, int location, Door attached, int attachedId) {
    door.put(direction, location);
    doorArray.put(direction, attached);
    doorAttached.put(attachedId, direction);
  }

  /**
   * Outdated method used to pass test suite.
   * @param dir direction
   * @param loc locastion
   */
  public void setDoor(String dir, int loc) {
    door.put(dir, loc);
  }

  /**
   * Returns the wall on wghich attached door is located.
   * @param attachedRoomId other room id
   * @return wall
   */
  public String getWall(int attachedRoomId) {
    return doorAttached.get(attachedRoomId);
  }


  /**
   * Get hashmap of doors.
   * @return doorarray hashmap of doors
   */
  public HashMap<String, Door> getDoorConnections() {
    return doorArray;
  }

  /**
   * Get hashmap of doors.
   * @return door hashmap of doors
   */
  public HashMap<String, Integer> getDoorPositions() {
    return door;
  }

  /**
   * return number of doors in hashmap.
   * @return size of hashmap doors
   */
  public int getNumDoors() {
    return doorArray.size();
  }

  /**
   * Set player in room.
   * @param isInRoom true / false depending wether player is in room or not
   */
  public void setPlayerInRoom(boolean isInRoom) {
    inRoom = isInRoom;
  }


  /**
   * Check if player is in current room.
   * @return boolean true if player is in room
   */
  public boolean isPlayerInRoom() {
    return inRoom;
  }

  /**
   * Setter for symbol of NS door.
   * @param ns String symbol to be set
   */
  public void setNsSymbol(String ns) {
    nsWall = ns;
  }

  /**
   * Getter for symbol of NS wall.
   * @return String symbol of wall
   */
  public String getNsSymbol() {
    return nsWall;
  }

  /**
   * Setter for symbol of ew door.
   * @param ew String symbol to be set
   */
  public void setEwSymbol(String ew) {
    ewWall = ew;
  }

  /**
   * Getter for symbol of EW wall.
   * @return String symbol of wall
   */
  public String getEwSymbol() {
    return ewWall;
  }

  /**
   * Setter for symbol of floor.
   * @param floorSymbol String symbol of floor
   */
  public void setFloorSymbol(String floorSymbol) {
    floor = floorSymbol;
  }

  /**
   * Getter of floor symbol.
   * @return String of floor symbol
   */
  public String getFloorSymbol() {
    return floor;
  }

  /**
   * Setter for door symbol.
   * @param doorSymb String for door symbol to be set
   */
  public void setDoorSymbol(String doorSymb) {
    doorSymbol = doorSymb;
  }

  /**
   * Getter for door symbol.
   * @return String of door symbol
   */
  public String getDoorSymbol() {
    return doorSymbol;
  }




   /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
    * @return (String) String representation of how the room looks
    */
  public String displayRoom() {
    roomString = ""; //+ "<<<< [ROOM " + id + "] >>>>" + System.lineSeparator();
    for (int i = 0; i < height; i++) {
      for (int k = 0; k < width; k++) {
        if (checkPlayer(i, k)) {
          checkDoorNs(i, k);
          checkDoorEw(i, k);
          checkRoomItems(i, k);
        }
      }
      roomString = roomString + System.lineSeparator(); 
    }
    return roomString;
}


  private void checkDoorNs(int i, int k) {
    if (i == 0) {
      if (door.get("N") == null || door.get("N") != k) {
        roomString = roomString + nsWall;
      } else {
        roomString = roomString + doorSymbol;
      }
    } else if (i == (height - 1)) {
      if (door.get("S") == null || door.get("S") != k) {
        roomString = roomString + nsWall;
      } else {
        roomString = roomString + doorSymbol;
      }
    }
  }

  private void checkDoorEw(int i, int k) {
    if (!checkIfEwWall(i, k)) {
      return;
    }
    if (k == 0) {
      if (door.get("W") != null && door.get("W") == i) {
        roomString = roomString + doorSymbol;
      } else {
          roomString = roomString + ewWall;
        }
      } else if (k == width - 1) {
        if (door.get("E") != null && door.get("E") == i) {
          roomString = roomString + doorSymbol;
        } else {
          roomString = roomString + ewWall;
        }
      }
  }


  private boolean checkPlayer(int i, int k) {
    if (player != null && (player.getXyLocation()).y == i && (player.getXyLocation()).x == k) {
      roomString = roomString + player.getSymbol();
      return false;
    }
    return true;
  }

  private void checkRoomItems(int i, int k) {
    if (!checkIfWall(i, k)) {
      return;
    }
    if (roomItems != null) {
      boolean check = false;
      for (int n = 0; n < roomItems.size(); n++) {
        Item temp = roomItems.get(n);
        if ((temp.getXyLocation()).y == i && (temp.getXyLocation()).x == k) {
          roomString = roomString + temp.getSymbol();
          check = true; break;
        }
      }
      if (!check) {
        roomString = roomString + floor;
      }
    } else {
      roomString = roomString + floor;
    }
  }


  private boolean checkIfWall(int i, int k) {
    if (i == 0 || i == height - 1) {
      return false;
    } else if (k == 0 || k == width - 1) {
      return false;
    }
    return true;
  }


  private boolean checkIfEwWall(int i, int k) {
    if (i == 0 || i == height - 1) {
      return false;
    }
    return true;
  }


  /**
   * Passes list of possible item ids to cross check.
   * @param possibleItems all possible item ids
   */
  public void setPossibleItems(ArrayList<Item> possibleItems) {
    itemIds = possibleItems;
  }


  /**
   * Adds item to room items.
   * @param toAdd item to be added
   * @throws ImpossiblePositionException
   * @throws NoSuchItemException
   */
  public void addItem(Item toAdd) throws NoSuchItemException, ImpossiblePositionException {
    int x = toAdd.getXyLocation().x;
    int y = toAdd.getXyLocation().y;
    boolean check = Rogue.itemExists(toAdd);
    if (!check) {
      throw new NoSuchItemException();
    }
    if (x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1) {
      throw new ImpossiblePositionException();
    }
    if (player != null && player.getXyLocation().x == x && player.getXyLocation().y == y) {
      throw new ImpossiblePositionException();
    }
    for (int i = 0; i < allRooms.size(); i++) {
      if (allRooms.get(i).getId() == id) {
        toAdd.setCurrentRoom(allRooms.get(i));
      }
    }
    roomItems.add(toAdd);
    itemOverlap(toAdd, x, y);
  }


  private void itemOverlap(Item toAdd, int x, int y) throws ImpossiblePositionException {
    for (int i = 0; i < roomItems.size(); i++) {
      if (roomItems.get(i) != null && roomItems.get(i) != toAdd) {
        if (x == roomItems.get(i).getXyLocation().x && y == roomItems.get(i).getXyLocation().y) {
          throw new ImpossiblePositionException();
        }
      }
    }
  }

  /**
   * Removes item from list.
   * @param toRemove
   */
  public void removeItem(Item toRemove) {
    roomItems.remove(toRemove);
  }


  /**
   * Finds an empty spot in dungeon.
   * @return point containing empty spot
   */
  public Point findEmpty() {
    boolean check = false;
    for (int i = 1; i < height - 1; i++) {
      for (int k = 2; k < width - 1; k++) {
        Point temp = new Point(k, i);
        check = checkValidPos(temp);
        if (!check) {
          return temp;
        }
      }
    }
    return null;
  }

  //NOT TESTED CHECK IF WORKS
  private boolean checkValidPos(Point temp) {
    for (int z = 0; z < roomItems.size(); z++) {
      if (temp.x == roomItems.get(z).getXyLocation().x && temp.y == roomItems.get(z).getXyLocation().y) {
        break;
      }
      if (player != null && player.getXyLocation().x == temp.x && player.getXyLocation().y == temp.y) {
        break;
      }
      if (z == roomItems.size() - 1) {
        return false;
      }
    }
    return true;
  }


  /**
   * Get array of all rooms in dungeon.
   * @param rooms arraylist of rooms
   */
  public void setAllRooms(ArrayList<Room> rooms) {
    allRooms = rooms;
  }


  /**
   * Verify room is possible with correct doors.
   * @return bool val true if room is verified
   * @throws NotEnoughDoorsException
   */
  public boolean verifyRoom() throws NotEnoughDoorsException {
    if (!checkItemInWall()) {
      return false;
    }
    if (!checkPlayerInWall()) {
      return false;
    }
    if (!checkAttachedDoors()) {
      return false;
    }
    if (doorArray.size() == 0 && door.size() == 0) {
      throw new NotEnoughDoorsException();
    }
    return true;
  }


  private boolean checkItemInWall() {
    for (int i = 0; i < roomItems.size(); i++) {
      int x = roomItems.get(i).getXyLocation().x;
      int y = roomItems.get(i).getXyLocation().y;
      if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
        return false; //Items are in walls not valid.
      }
    }
    return true;
  }


  private boolean checkPlayerInWall() {
    if (player != null) {
      int x = player.getXyLocation().x;
      int y = player.getXyLocation().y;
      if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
        return false; //player is in walls not valid.
      }
    }
    return true;
  }


  private boolean checkAttachedDoors() {
    if (doorArray.get("N") != null && doorArray.get("N").getConnectedRooms().size() != 2) {
      return false; //unattached door
    }
    if (doorArray.get("S") != null && doorArray.get("S").getConnectedRooms().size() != 2) {
      return false; //unattached door
    }
    if (doorArray.get("E") != null && doorArray.get("E").getConnectedRooms().size() != 2) {
      return false; //unattached door
    }
    if (doorArray.get("W") != null && doorArray.get("W").getConnectedRooms().size() != 2) {
      return false; //unattached door
    }
    return true;
  }

  /**
   * Fixes not enough doors exception.
   */
  public void fixNotEnoughDoors() {
    Room connect = null;
    Room curr = null;
    for (int i = 0; i < allRooms.size(); i++) {
      if (allRooms.get(i).getId() == id) {
        curr = allRooms.get(i);
        break;
      }
    }
    connect = checkValidDoors();
    if (connect == null) {
      System.out.println("Dungeon file cannot be used due to Door error.");
      System.exit(0);
    }
    addNewDoorFix(connect, curr);
  }


  private Room checkValidDoors() {
    Room connect = null;
    for (int i = 0; i < allRooms.size(); i++) {
      if (allRooms.get(i).getNumDoors() != 0 && allRooms.get(i).getNumDoors() < (2 * 2)) {
        return allRooms.get(i);
      }
      if (i == allRooms.size() - 1) {
        System.out.println("Dungeon file cannot be used due to Door error.");
        System.exit(0);
      }
    }
    return null;
  }

  private void addNewDoorFix(Room connect, Room curr) {
    Door temp = new Door();
    if (connect.getDoorConnections().get("N") == null) {
    temp.connectRoom(connect, "N"); temp.connectRoom(curr, "N"); setDoor("N", (int) (width / 2), temp, connect.getId());
      connect.setDoor("N", (int) (connect.getWidth() / 2), temp, id);
      return;
    } else if (connect.getDoorConnections().get("S") == null) {
    temp.connectRoom(connect, "S"); temp.connectRoom(curr, "S"); setDoor("S", (int) (width / 2), temp, connect.getId());
      connect.setDoor("S", (int) (connect.getWidth() / 2), temp, id);
      return;
    } else if (connect.getDoorConnections().get("E") == null) {
    temp.connectRoom(connect, "E"); temp.connectRoom(curr, "E"); setDoor("E", (int) (width / 2), temp, connect.getId());
      connect.setDoor("E", (int) (connect.getWidth() / 2), temp, id);
      return;
    } else if (connect.getDoorConnections().get("W") == null) {
    temp.connectRoom(connect, "W"); temp.connectRoom(curr, "W"); setDoor("W", (int) (width / 2), temp, connect.getId());
      connect.setDoor("W", (int) (connect.getWidth() / 2), temp, id);
      return;
    }
  }

}

