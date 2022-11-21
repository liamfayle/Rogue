package rogue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import rogue.*;
import java.awt.Point;

public class RogueClassTests {

    /**
     * Test the Width getter and setter Expected: Width that was passed into the
     * setter
     */
    @Test
    public void testPlayer_GetterSetter() {
        Player testPlayer = new Player();
        Rogue rogue = new Rogue();

        rogue.setPlayer(testPlayer);
        assertEquals("Field wasn't retrieved/set properly", testPlayer, rogue.getPlayer());
    }

    @Test
    public void testGetNextDisplay_startingRoom() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        String room = "----+---------------\n|@.................|\n|..................|\n|..!...............|\n|..................|\n|..................|\n|..................|\n+..................|\n|.......?..........|\n-+------------------";
        final Rogue rogue = new Rogue(parser);

        // System.out.println("Should be:");
        // System.out.println(room);
        // System.out.println("ours:");
        // System.out.println(rogue.getNextDisplay());

        assertEquals("Invalid starting display", room.replaceAll("\\s", ""),
                rogue.getNextDisplay().replaceAll("\\s", ""));
    }

    @Test
    public void testGetNextDisplay_withMakeMove_valid_down() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        String room = "----+---------------\n|..................|\n|@.................|\n|..!...............|\n|..................|\n|..................|\n|..................|\n+..................|\n|.......?..........|\n-+------------------";

        final Rogue rogue = new Rogue(parser);
        try {
            rogue.makeMove(rogue.DOWN);
        } catch (InvalidMoveException e) {
            assertTrue("Caught a InvalidMoveException", false);
        } catch (Exception e) {
            assertTrue("Caught an exception", false);
        }

        assertEquals("Invalid display", room.replaceAll("\\s", ""), rogue.getNextDisplay().replaceAll("\\s", ""));
    }

    @Test
    public void testGetNextDisplay_withMakeMove_invalid_hitWall() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");

        final Rogue rogue = new Rogue(parser);
        try {
            rogue.makeMove(rogue.LEFT);
            assertTrue("Did not throw a InvalidMoveException", false);
        } catch (InvalidMoveException e) {
            assertTrue(true);
        } catch (Exception e) {
            assertTrue("Caught an exception", false);
        }
    }

    @Test
    public void testRooms_GetterSetter() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        final Rogue rogue = new Rogue(parser);

        HashMap<String, String> mapRoom1 = new HashMap<>();
        mapRoom1.put("id", "3");
        mapRoom1.put("width", "20");
        mapRoom1.put("height", "10");

        rogue.addRoom(mapRoom1);

        ArrayList<Room> rooms = rogue.getRooms();
        for (Room room : rooms) {
            if (room.getId() == 3 && room.getWidth() == 20 && room.getHeight() == 10) {
                assertTrue(true);
                return;
            }
        }

        assertTrue("Could not add room", false);

    }

    @Test
    public void testItems_GetterSetter() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        final Rogue rogue = new Rogue(parser);

        HashMap<String, String> mapItem1 = new HashMap<>();
        mapItem1.put("room", "1");
        mapItem1.put("id", "3");
        mapItem1.put("name", "Mango");
        mapItem1.put("type", "food");
        mapItem1.put("description", "my that was a yummy mango!");
        mapItem1.put("x", "4");
        mapItem1.put("y", "8");

        rogue.addItem(mapItem1);

        Item tempItem = new Item();
        tempItem.setId(3);
        tempItem.setDescription("my that was a yummy mango!");
        tempItem.setName("Mango");
        tempItem.setType("food");
        Point point = new Point(4, 8);
        tempItem.setXyLocation(point);

        ArrayList<Room> rooms = rogue.getRooms();
        for(Room room: rooms) {
            if(room.getId() == 1) {
                ArrayList<Item> items = room.getRoomItems();

                for (Item item : items) {
                    if (item.getId() == tempItem.getId() && item.getName() == tempItem.getName()
                            && item.getType() == tempItem.getType() && item.getDescription() == tempItem.getDescription()
                            && item.getXyLocation().equals(tempItem.getXyLocation())) {
                        assertTrue(true);
                        return;
                    }
                }
            }
        }

        assertTrue("Could not add item", false);

    }

}
