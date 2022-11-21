package rogue;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;

import rogue.*;

import java.awt.Point;

public class DoorClassTests {

    /**
     * Test the connectedRooms Expected: the arraylist of rooms connected by the
     * door
     */
    @Test
    public void testConnectRoomAndGetConnectedRooms() {
        Door door = new Door();
        ArrayList<Room> rooms = new ArrayList();
        Room room1 = new Room();
        Room room2 = new Room();

        rooms.add(room1);
        rooms.add(room2);

        door.connectRoom(room1);
        door.connectRoom(room2);

        ArrayList<Room> resultingRooms = door.getConnectedRooms();

        for (int i = 0; i < rooms.size(); i++) {
            if (i < resultingRooms.size()) {
                Room expectedRoom = rooms.get(i);
                Room testRoom = resultingRooms.get(i);
                assertEquals("Field wasn't retrieved/set properly", testRoom, expectedRoom);
            }
        }
    }

    /**
     * Test the get other room Expected:
     */
    @Test
    public void testGetOtherRoom() {
        Door door = new Door();

        ArrayList<Room> rooms = new ArrayList();
        Room room1 = new Room();

        Room room2 = new Room();

        rooms.add(room1);
        rooms.add(room2);

        door.connectRoom(room1);
        door.connectRoom(room2);

        Room resultingRoom = door.getOtherRoom(room1);
        assertEquals("Field wasn't retrieved/set properly", resultingRoom, room2);
    }
}
