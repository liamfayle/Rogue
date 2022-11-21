package rogue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;

import rogue.*;

import java.awt.Point;

public class RoomClassTests {

    /**
     * Test the Width getter and setter Expected: Width that was passed into the
     * setter
     */
    @Test
    public void testWidth_GetterSetter() {
        int testWidth = 20;

        final Room room = new Room();
        room.setWidth(testWidth);

        assertEquals("Field wasn't retrieved/set properly", testWidth, room.getWidth());
    }

    /**
     * Test the Height getter and setter Expected: Height that was passed into the
     * setter
     */
    @Test
    public void testHeight_GetterSetter() {
        int testHeight = 20;

        final Room room = new Room();
        room.setHeight(testHeight);

        assertEquals("Field wasn't retrieved/set properly", testHeight, room.getHeight());
    }

    /**
     * Test the Id getter and setter Expected: Id that was passed into the setter
     */
    @Test
    public void testId_GetterSetter() {
        int testId = 2;

        final Room room = new Room();
        room.setId(testId);

        assertEquals("Field wasn't retrieved/set properly", testId, room.getId());
    }

    /**
     * Test the room items getter and setter Expected: ArrayList of Items in the
     * room that gets passed into the setter
     */
    @Test
    public void testRoomItems_GetterSetter() {
        ArrayList<Item> testItemsList = new ArrayList();
        Item item_1 = new Item();
        Item item_2 = new Item();
        testItemsList.add(item_1);
        testItemsList.add(item_2);

        final Room room = new Room();
        room.setRoomItems(testItemsList);

        assertEquals("Field wasn't retrieved/set properly", testItemsList, room.getRoomItems());
    }

    /**
     * Test the player getter and setter Expected: Player object that gets passed
     * into the setter
     */
    @Test
    public void testPlayer_GetterSetter() {
        Player testPlayer = new Player();

        final Room room = new Room();
        room.setPlayer(testPlayer);

        assertEquals("Field wasn't retrieved/set properly", testPlayer, room.getPlayer());
    }

    /**
     * Test if the player is in the room Expected: True
     */
    @Test
    public void testIsPlayerInRoom_true() {
        Player testPlayer = new Player();

        final Room room = new Room();
        room.setPlayer(testPlayer);
        testPlayer.setCurrentRoom(room);

        assertEquals("Did not return true", true, room.isPlayerInRoom());
    }

    /**
     * Test if the player is in the room Expected: false
     */
    @Test
    public void testIsPlayerInRoom_false() {
        final Room room = new Room();
        room.setWidth(20);
        room.setHeight(10);

        assertEquals("Did not return false", false, room.isPlayerInRoom());
    }

    /**
     * Test if the room has enough doors Expected: true
     */
    @Test
    public void testIsRoomValid_false() {
        final Room room = new Room();
        room.setWidth(20);
        room.setHeight(10);

        try {
            assertEquals("Did not return false", false, room.verifyRoom());
        } catch (NotEnoughDoorsException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    /**
     * Test if the room has enough doors Expected: true
     */
    @Test
    public void testIsRoomValid_true() {
        final Room room = new Room();
        room.setWidth(20);
        room.setHeight(10);

        ArrayList<Door> doors = new ArrayList();

        room.setDoor("N", 1);
        room.setDoor("S", 3);

        try {
            assertEquals("Did not return true", true, room.verifyRoom());
        } catch (NotEnoughDoorsException e) {
            assertTrue("Not enough doors", false);
        } catch (Exception e) {
            assertTrue("Got an exception", false);
        }
    }

    /**
     * Test for adding item
     */
    @Test
    public void testAddItem_NoSuchItemException() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        final Rogue rogue = new Rogue(parser);

        HashMap<String, String> mapItem1 = new HashMap<>();
        mapItem1.put("room", "1");
        mapItem1.put("id", "10");
        mapItem1.put("name", "Health Potion");
        mapItem1.put("type", "consumable");
        mapItem1.put("description", "you feel better");
        mapItem1.put("x", "4");
        mapItem1.put("y", "8");

        rogue.addItem(mapItem1);
        ArrayList<Item> items = rogue.getItems();

        final Room room = new Room();
        room.setRoomItems(items);
        room.setWidth(10);
        room.setHeight(10);

        Item tempItem = new Item();
        tempItem.setId(1);
        tempItem.setDescription("des des des");
        tempItem.setName("name");
        tempItem.setType("type");
        Point point = new Point(1, 1);
        tempItem.setXyLocation(point);

        try {
            room.addItem(tempItem);
            assertTrue("Did not throw a NoSuchItemException", false);
        } catch (ImpossiblePositionException e) {
            assertTrue("ImpossiblePositionException thrown", false);
        } catch (NoSuchItemException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue("Failed with exception", false);
        }
    }

    /**
     * Test for adding item
     */
    @Test
    public void testAddItem_ImpossiblePositionException() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        final Rogue rogue = new Rogue(parser);

        HashMap<String, String> mapItem1 = new HashMap<>();
        mapItem1.put("room", "1");
        mapItem1.put("id", "10");
        mapItem1.put("name", "Health Potion");
        mapItem1.put("type", "consumable");
        mapItem1.put("description", "you feel better");
        mapItem1.put("x", "4");
        mapItem1.put("y", "8");

        rogue.addItem(mapItem1);
        ArrayList<Item> items = rogue.getItems();

        final Room room = new Room();
        room.setRoomItems(items);
        room.setId(1);
        room.setWidth(10);
        room.setHeight(10);

        Item tempItem = new Item();
        tempItem.setId(10);
        tempItem.setDescription("you feel better");
        tempItem.setName("Health Potion");
        tempItem.setType("consumable");
        Point point = new Point(-1, -1);
        tempItem.setXyLocation(point);

        try {
            room.addItem(tempItem);
            assertTrue("Did not throw a ImpossiblePositionException", false);
        } catch (ImpossiblePositionException e) {
            assertTrue(true);
        } catch (NoSuchItemException e) {
            assertTrue("NoSuchItemException thrown", false);
        } catch (Exception e) {
            assertTrue("Failed with exception", false);
        }
    }

        /**
     * Test for adding item
     */
    @Test
    public void testAddItem_CurrentRoomID() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        final Rogue rogue = new Rogue(parser);

        ArrayList<Room> rooms = rogue.getRooms();
        Room testRoom = new Room();

        for(Room room: rooms) {
            if(room.getId() == 1) {
                testRoom = room;
            }
        }

        Item tempItem = new Item();
        tempItem.setId(3);
        tempItem.setDescription("my that was a yummy mango!");
        tempItem.setName("Mango");
        tempItem.setType("food");
        Point point = new Point(4, 8);
        tempItem.setXyLocation(point);

        try {
            testRoom.addItem(tempItem);
            Room itemsRoom = tempItem.getCurrentRoom();
            assertEquals("Item room ID is not set to expected room ID", testRoom.getId(), itemsRoom.getId());
        } catch (ImpossiblePositionException e) {
            assertTrue("ImpossiblePositionException thrown", false);
        } catch (NoSuchItemException e) {
            assertTrue("NoSuchItemException thrown", false);
        } catch (Exception e) {
            assertTrue("Failed with other exception", false);
        }
    }
}
