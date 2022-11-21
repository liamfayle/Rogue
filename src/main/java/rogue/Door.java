package rogue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class Door implements Serializable {
    private ArrayList<Room> connectedRooms;
    private ArrayList<String> attachedWall;

    /**
     * Default constructor for door class.
     */
    public Door() {
        connectedRooms = new ArrayList<>();
        attachedWall = new ArrayList<>();
    }


    /**
     * Add room to connectedrooms arraylist.
     * @param r new room to be added
     * @param wall wall, connected
     */
    public void connectRoom(Room r, String wall) {
        connectedRooms.add(r);
        attachedWall.add(wall);
    }

    /**
     * Returns arraylist of both rooms connected to door.
     * @return connectedrooms
     */
    public ArrayList<Room> getConnectedRooms() {
        return connectedRooms;
    }

    /**
     * Returns rooms connected to door attached to current room.
     * @param currentRoom currentroom player is in
     * @return next room through door
     */
    public Room getOtherRoom(Room currentRoom) {
        int index = connectedRooms.indexOf(currentRoom);
        if (index == 0) {
            return connectedRooms.get(1);
        } else if (index == 1) {
            return connectedRooms.get(0);
        }
        return null;
    }

    /**
     * Returns attached wall string.
     * @param currentRoom current room
     * @return attached wall
     */
    public String getAttachedWall(Room currentRoom) {
        int index = connectedRooms.indexOf(currentRoom);
        if (index == 0) {
            return attachedWall.get(1);
        } else if (index == 1) {
            return attachedWall.get(0);
        }
        return null;
    }

    /**
     * get position of corresponding door.
     * @param currentRoom room player is in
     * @return position he should spawn in new room
     */
    public int getPos(Room currentRoom) {
        Room connected = getOtherRoom(currentRoom);
        String wall = getAttachedWall(currentRoom);
        HashMap<String, Integer> connectedDoors = connected.getDoorPositions();
        return connectedDoors.get(wall);
    }

    /**
     * Outdated method only here to pass A2 test cases.
     * @param room room passed
     */
    public void connectRoom(Room room) {
        connectedRooms.add(room);
    }

}
