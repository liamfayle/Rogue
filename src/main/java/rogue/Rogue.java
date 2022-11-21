package rogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import java.awt.Point;
import java.io.Serializable;


public class Rogue implements Serializable {
    private ArrayList<Room> rooms;
    private static ArrayList<Item> items;
    private ArrayList<Map<String, String>> itemsMap;
    private HashMap<String, String> symbols;
    private Room currentRoom;
    private Player player;
    private HashMap<String, Door> toBeConnected;
    private transient RogueParser parser;
    private Inventory inv;
    private int itemIndex = 0;
    private Item selected;
    private String lastOutput;
    public static final char UP = '8';
    public static final char DOWN = '2';
    public static final char LEFT = '4';
    public static final char RIGHT = '6';
    public static final char EAT = 'e';
    public static final char WEAR = 'w';
    public static final char THROW = 't';
    public static final char SCROLL_UP = 'd';
    public static final char SCROLL_DOWN = 'a';
    //private static final long serialVersionUID = 4L;


    /**
     * default constructor.
     */
    public Rogue() {
        rooms = new ArrayList<Room>();
        items = new ArrayList<Item>();
        itemsMap = new ArrayList<>();
        symbols = new HashMap<String, String>();
        toBeConnected = new HashMap<String, Door>();
        inv = new Inventory();
    }

    /**
     * Constructor with parser passed.
     * @param newParser parsed jason data from rogueparse class
     */
    public Rogue(RogueParser newParser) {
        rooms = new ArrayList<Room>();
        items = new ArrayList<Item>();
        itemsMap = new ArrayList<>();
        symbols = new HashMap<String, String>();
        toBeConnected = new HashMap<String, Door>();
        inv = new Inventory();
        parser = newParser;
        setSymbols();
        setRooms();
        setItems();
        getStartRoom();
        setPlayer(new Player());
    }

    /**
     * Method to set player in position in room.
     * @param thePlayer The player instance to be placed
     */
    public void setPlayer(Player thePlayer) {
        player = thePlayer;
        player.setXyLocation(new Point(1, 1));
        player.setSymbol(symbols.get("PLAYER"));
        for (int i = 0; i < rooms.size(); i++) {
            Room temp = rooms.get(i);
            if (temp.isPlayerInRoom()) {
                temp.setPlayer(player);
                break;
            }
        }
    }


    private void setSymbols() {
        symbols = parser.getSymbols();
    }

    /**
     * Set parsed room data.
     */
    private void setRooms() {
        Map room = parser.nextRoom();
        while (room != null) {
            addRoom(room); 
            room = parser.nextRoom();
        }
        boolean check = false;
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).setAllRooms(rooms);
        }
        for (int i = 0; i < rooms.size(); i++) {
            try {
                check = rooms.get(i).verifyRoom();
                if (!check) {
                    rooms.remove(i);
                }
            } catch (NotEnoughDoorsException e) {
                rooms.get(i).fixNotEnoughDoors();
            }
        }
    }


    /*private void setItems() {
        items = parser.getItems(); Used only with custom parsr.
    }*/

    /**
     * Getter for Array of all rooms.
     * @return ArrayList of all rooms
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Getter for Array of all items.
     * @return ArrayList of all items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Getter for player instance.
     * @return Player instance
     */
    public Player getPlayer() {
        return player;
    }


    private void getStartRoom() {
        for (Room room : rooms) {
            if (room.isPlayerInRoom()) {
                currentRoom = room;
                break;
            }
        }
    }


    /**
     * updates display once move has been made.
     * @param input move direction
     * @return string that move has been made
     * @throws InvalidMoveException
     */
    public String makeMove(char input) throws InvalidMoveException {
        if (input == UP) {
            return moveUp(player.getXyLocation().x, player.getXyLocation().y);
        } else if (input == DOWN) {
            return moveDown(player.getXyLocation().x, player.getXyLocation().y);
        } else if (input == LEFT) {
            return moveLeft(player.getXyLocation().x, player.getXyLocation().y);
        } else if (input == RIGHT) {
            return moveRight(player.getXyLocation().x, player.getXyLocation().y);
        } else if (input == EAT) {
            return eat();
        } else if (input == WEAR) {
            return wear();
        } else if (input == THROW) {
            return toss();
        } else if (input == SCROLL_DOWN || input == SCROLL_UP) {
            selectItem(input); return "Searching your bag!"; //CAN ADD INV SCROLLING STRING (Change color in inv)
        }
        return "Invalid Key Pressed.";
    }


    private String eat() { //either food or potion or small food
        String s;
        if (inv.getItems().size() == 0) {
            return "Inventory empty, try Picking up an item!";
        }
        int index = eatFood();
        if (index != -1) {
            s = inv.getFood().get(index).eat(); inv.removeItem(selected); resetIndex(itemIndex); return s;
        }
        index = eatPotion();
        if (index != -1) {
            s = inv.getPotion().get(index).eat(); inv.removeItem(selected); resetIndex(itemIndex); return s;
        }
        index = eatSmallFood();
        if (index != -1) {
            s = inv.getSmallFood().get(index).eat(); inv.removeItem(selected); resetIndex(itemIndex); return s;
        }
        return "Item cannot be consumed!";
    }


    private void resetIndex(int curr) {
        if (inv.getItems().size() > 0) {
            itemIndex -= 1;
            itemIndex = checkOverflow(itemIndex);
            selected = inv.getItems().get(itemIndex);
        } else {
            return;
        }
    }


    private int eatFood() {
        int id = selected.getId();
        int index = -1;
        ArrayList<Food> food = inv.getFood();
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }


    private int eatPotion() {
        int id = selected.getId();
        int index = -1;
        ArrayList<Potion> potion = inv.getPotion();
        for (int i = 0; i < potion.size(); i++) {
            if (potion.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }


    private int eatSmallFood() {
        int id = selected.getId();
        int index = -1;
        ArrayList<SmallFood> smallFood = inv.getSmallFood();
        for (int i = 0; i < smallFood.size(); i++) {
            if (smallFood.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }




    private String wear() { //either clothing or ring
        if (inv.getItems().size() == 0) {
            return "Inventory empty, try Picking up an item!";
        }
        int index = wearClothing();
        if (index != -1) {
            String string = inv.getClothing().get(index).wear();
            inv.removeItem(selected); resetIndex(itemIndex);
            return string;
        }
        index = wearRing();
        if (index != -1) {
            String string = inv.getRing().get(index).wear();
            inv.removeItem(selected); resetIndex(itemIndex);
            return string;
        }
        return "Item cannot be worn!";
    }


    /**
     * Returns current list of items in inv.
     * @return its string array
     */
    public String[] getInventoryItems() {
        String[] its = new String[inv.getItems().size()];
        for (int i = 0; i < inv.getItems().size(); i++) {
            its[i] = inv.getItems().get(i).getName();
        }
        return its;
    }

    /**
     * returns index of selected item.
     * @return itemIndex
     */
    public int getSelected() {
        return itemIndex;
    }

    private int wearClothing() {
        int id = selected.getId();
        int index = -1;
        ArrayList<Clothing> clothing = inv.getClothing();
        for (int i = 0; i < clothing.size(); i++) {
            if (clothing.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }

    private int wearRing() {
        int id = selected.getId();
        int index = -1;
        ArrayList<Ring> ring = inv.getRing();
        for (int i = 0; i < ring.size(); i++) {
            if (ring.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }



    private String toss() { //either potion or smallfood
        if (inv.getItems().size() == 0) {
            return "Inventory empty, try Picking up an item!";
        }
        int index = tossPotion();
        if (index != -1) {
            String string = inv.getPotion().get(index).toss();
            selected.setXyLocation(player.getXyLocation());
            currentRoom.addItemInit(selected); 
            inv.removeItem(selected); 
            resetIndex(itemIndex);
            return string;
        }
        index = tossFood();
        if (index != -1) {
            String string = inv.getSmallFood().get(index).toss();
            selected.setXyLocation(player.getXyLocation());
            currentRoom.addItemInit(selected); 
            inv.removeItem(selected); 
            resetIndex(itemIndex);
            return string;
        }
        return "Item cannot be thrown away!";
    }

    private int tossPotion() {
        int id = selected.getId();
        int index = -1;
        ArrayList<Potion> potion = inv.getPotion();
        for (int i = 0; i < potion.size(); i++) {
            if (potion.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }

    private int tossFood() {
        int id = selected.getId();
        int index = -1;
        ArrayList<SmallFood> food = inv.getSmallFood();
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).getId() == id) {
                index = i;
            }
        }
        return index;
    }



    private void selectItem(char input) {
        if (inv.getItems().size() == 0) {
            return;
        }
        if (input == SCROLL_DOWN) {
            itemIndex -= 1;
            itemIndex = checkOverflow(itemIndex);
            selected = inv.getItems().get(itemIndex);
        } else if (input == SCROLL_UP) {
            itemIndex += 1;
            itemIndex = checkOverflow(itemIndex);
            selected = inv.getItems().get(itemIndex);
        }
    }


    private int checkOverflow(int i) {
        if (i < 0) {
            return inv.getItems().size() - 1;
        } else if (i > inv.getItems().size() - 1) {
            return 0;
        }
        return i;
    }


    /**
     * Executes up move.
     * @param x player x coords
     * @param y player y coords
     * @return move string
     * @throws InvalidMoveException
     */
    private String moveUp(int x, int y) throws InvalidMoveException {
        Map<String, Door> dCon = currentRoom.getDoorConnections();
        Map<String, Integer> d = currentRoom.getDoorPositions();
        if (y == 1 && d.get("N") != null && x == d.get("N")) {
            enterDoor("N");
            return "Moved through a door!";
        } else if (y != 1) {
            moveDir("N");
            String check = checkForItem();
            if (check == null) {
                return "Moved UP";
            } else {
                return check;
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Executes down move.
     * @param x player x coords
     * @param y player y coords
     * @return move string
     * @throws InvalidMoveException
     */
    private String moveDown(int x, int y) throws InvalidMoveException {
        Map<String, Door> dCon = currentRoom.getDoorConnections();
        Map<String, Integer> d = currentRoom.getDoorPositions();
        if (y == currentRoom.getHeight() - 2 && d.get("S") != null && x == d.get("S")) {
            enterDoor("S");
            return "Moved through a door!";
        } else if (y != currentRoom.getHeight() - 2) {
            moveDir("S");
            String check = checkForItem();
            if (check == null) {
                return "Moved DOWN";
            } else {
                return check;
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Executes left move.
     * @param x player x coords
     * @param y player y coords
     * @return move string
     * @throws InvalidMoveException
     */
    private String moveLeft(int x, int y) throws InvalidMoveException {
        Map<String, Door> dCon = currentRoom.getDoorConnections();
        Map<String, Integer> d = currentRoom.getDoorPositions();
        if (x == 1 && d.get("W") != null && y == d.get("W")) {
            enterDoor("W");
            return "Moved through a door!";
        } else if (x != 1) {
            moveDir("W");
            String check = checkForItem();
            if (check == null) {
                return "Moved LEFT";
            } else {
                return check;
            }
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Executes right move.
     * @param x player x coords
     * @param y player y coords
     * @return move string
     * @throws InvalidMoveException
     */
    private String moveRight(int x, int y) throws InvalidMoveException {
        Map<String, Door> dCon = currentRoom.getDoorConnections();
        Map<String, Integer> d = currentRoom.getDoorPositions();
        if (x == currentRoom.getWidth() - 2 && d.get("E") != null && y == d.get("E")) {
            enterDoor("E");
            return "Moved through a door!";
        } else if (x != currentRoom.getWidth() - 2) {
            moveDir("E");
            String check = checkForItem();
            if (check == null) {
                return "Moved RIGHT";
            } else {
                return check;
            }
        } else {
            throw new InvalidMoveException();
        }
    }


    private void enterDoor(String dir) {
        Map<String, Door> doorConections = currentRoom.getDoorConnections();
        currentRoom.setPlayerInRoom(false);
        int pos = doorConections.get(dir).getPos(currentRoom);
        String wall = doorConections.get(dir).getAttachedWall(currentRoom);
        currentRoom = doorConections.get(dir).getOtherRoom(currentRoom);
        Point newPlayerLocation = handleDoorEntry(pos, wall, currentRoom.getHeight(), currentRoom.getWidth());
        int x = newPlayerLocation.x;
        int y = newPlayerLocation.y;
        currentRoom.setPlayerInRoom(true);
        player.setCurrentRoom(currentRoom);
        player.setXyLocation(new Point(x, y));
        currentRoom.setPlayer(player);
    }


    private Point handleDoorEntry(int pos, String wall, int height, int width) {
        if (wall == "N") {
            return new Point(pos, 1);
        } else if (wall == "S") {
            return new Point(pos, height - 2);
        } else if (wall == "W") {
            return new Point(1, pos);
        } else if (wall == "E") {
            return new Point(width - 2, pos);
        }
        return new Point(1, 1);
    }

    private String checkForItem() {
        ArrayList<Item> roomItems = currentRoom.getRoomItems();
        int playerX = player.getXyLocation().x;
        int playerY = player.getXyLocation().y;
        String name = " ";
        for (int i = 0; i < roomItems.size(); i++) {
            int itemX = roomItems.get(i).getXyLocation().x;
            int itemY = roomItems.get(i).getXyLocation().y;
            if (playerX == itemX && playerY == itemY) {
                inv.addItem(roomItems.get(i));
                selected = roomItems.get(i);
                itemIndex = inv.getItems().size() - 1;
                name = roomItems.get(i).getName();
                roomItems.remove(i);
                currentRoom.setRoomItems(roomItems);
                return "Picked up " + name;
            }
        }
        return null;
    }

    private void moveDir(String dir) {
        int playerX = player.getXyLocation().x;
        int playerY = player.getXyLocation().y;
        if (dir == "N") {
            player.setXyLocation(new Point(playerX, playerY - 1));
            currentRoom.setPlayer(player);
        } else if (dir == "S") {
            player.setXyLocation(new Point(playerX, playerY + 1));
            currentRoom.setPlayer(player);
        } else if (dir == "W") {
            player.setXyLocation(new Point(playerX - 1, playerY));
            currentRoom.setPlayer(player);
        } else if (dir == "E") {
            player.setXyLocation(new Point(playerX + 1, playerY));
            currentRoom.setPlayer(player);
        }
    }

    /**
     * Returns updated display after move is made.
     * @return display string
     */
    public String getNextDisplay() {
        return currentRoom.displayRoom();
    }


    /**
     * Add new room to game given map<string, string>.
     * @param toAdd map to be parsed and added
     */
    public void addRoom(Map<String, String> toAdd) {
        Room newRoom = new Room();
        newRoom.setEwSymbol(symbols.get("EW_WALL"));
        newRoom.setNsSymbol(symbols.get("NS_WALL"));
        newRoom.setFloorSymbol(symbols.get("FLOOR"));
        newRoom.setDoorSymbol(symbols.get("DOOR"));
        newRoom.setHeight(Integer.parseInt(toAdd.get("height")));
        newRoom.setId(Integer.parseInt(toAdd.get("id")));
        newRoom.setWidth(Integer.parseInt(toAdd.get("width")));
        newRoom.setPlayerInRoom(Boolean.parseBoolean(toAdd.get("start")));
        handleDoors(newRoom, toAdd);
        rooms.add(newRoom);
        connectUnconnected();
    }


    private void handleDoors(Room newRoom, Map<String, String> toAdd) {
        handleDoorsIndividual("N", newRoom, toAdd);
        handleDoorsIndividual("S", newRoom, toAdd);
        handleDoorsIndividual("E", newRoom, toAdd);
        handleDoorsIndividual("W", newRoom, toAdd);
    }


    private void handleDoorsIndividual(String dir, Room newRoom, Map<String, String> toAdd) {
        if (toAdd.get(dir) != null && !toAdd.get(dir).equals("-1")) {
            Door temp = new Door(); temp.connectRoom(newRoom, dir);
        newRoom.setDoor(dir, Integer.parseInt(toAdd.get(dir)), temp, Integer.parseInt(toAdd.get(dir + "connected")));
            connectDoor(toAdd, dir, temp, newRoom);
        }
    }


    private void connectDoor(Map<String, String> toAdd, String dir, Door door, Room newRoom) {
        int connected = Integer.parseInt(toAdd.get(dir + "connected").replace("connected", ""));
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == connected) {
                door.connectRoom(rooms.get(i), rooms.get(i).getWall(newRoom.getId()));
                return;
            }
        }
        toBeConnected.put(dir + Integer.toString(connected), door);
    }


    private void connectUnconnected() {
        for (int i = 0; i < rooms.size(); i++) {
            int id = rooms.get(i).getId();
            String roomId = Integer.toString(id);
            helpConnectUnconnected("N", roomId, id, i);
            helpConnectUnconnected("S", roomId, id, i);
            helpConnectUnconnected("E", roomId, id, i);
            helpConnectUnconnected("W", roomId, id, i);
        }
    }


    private void helpConnectUnconnected(String dir, String roomId, int id, int i) {
        if (toBeConnected.get(dir + roomId) != null) {
            Door door = toBeConnected.get(dir + roomId);
            Room attached = door.getConnectedRooms().get(0);
            door.connectRoom(rooms.get(i), rooms.get(i).getWall(attached.getId()));
            toBeConnected.remove(dir + roomId);
        }
    }


    private void setItems() {
        Map item = parser.nextItem();
        while (item != null) {
            addItem(item);
            item = parser.nextItem();
        }
    }

    /**
     * Add item to game.
     * @param toAdd map with json info
     */
    public void addItem(Map<String, String> toAdd) {
        Item newItem = new Item();
        if (toAdd.get("x") == null) {
            return;
        }
        int x = Integer.parseInt(toAdd.get("x"));
        int y = Integer.parseInt(toAdd.get("y"));
        String symb = symbols.get(toAdd.get("type").toUpperCase());
        newItem.setSymbol(symb);
        newItem.setDescription(toAdd.get("description"));
        newItem.setId(Integer.parseInt(toAdd.get("id")));
        newItem.setName(toAdd.get("name"));
        newItem.setType(toAdd.get("type"));
        newItem.setXyLocation(new Point(x, y));
        itemXyLoc(newItem, toAdd);
    }


    private void itemXyLoc(Item newItem, Map<String, String> toAdd) {
        for (int i = 0; i < rooms.size(); i++) {
            Room temp = rooms.get(i);
            if (temp.getId() == Integer.parseInt(toAdd.get("room"))) {
                newItem.setCurrentRoom(temp);
                items.add(newItem);
                try {
                    temp.addItem(newItem);
                } catch (NoSuchItemException e) {
                    temp.removeItem(newItem);
                } catch (ImpossiblePositionException e) {
                    newItem.setXyLocation(temp.findEmpty());
                }
                break;
            }
        }
    }


    /**
     * Checks if item is in possible items.
     * @param it checked item
     * @return true / false
     */
    static boolean itemExists(Item it) {
        for (int i = 0; i < items.size(); i++) {
            if (it.getId() == items.get(i).getId() && it.getName().equals(items.get(i).getName())) {
                return true;
            }
        }
        return false;
    }




}
