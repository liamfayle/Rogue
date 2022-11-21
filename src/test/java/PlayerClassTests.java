package rogue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.lang.reflect.Field;
import java.util.ArrayList; 
import org.junit.*;

import rogue.*;

import java.awt.Point;

public class PlayerClassTests {


    /**
     * Test the name getter and setter
     * Expected: name that was passed into the setter
     */
    @Test
    public void testName_GetterSetter() {
        String testName = "Mackenzie";

        final Player player = new Player();
        player.setName(testName);

        assertEquals("Field wasn't retrieved/set properly", testName, player.getName());
    }

    /**
     * Test the xy location getter and setter
     * Expected: xy location that was passed into the setter
     */
    @Test
    public void testLocation_GetterSetter() {
        Point testPoint = new Point(12,5);

        final Player player = new Player();
        player.setXyLocation(testPoint);

        assertEquals("Field wasn't retrieved/set properly", testPoint, player.getXyLocation());
    }

    /**
     * Test the player getter and setter
     * Expected: Player object that gets passed into the setter
     */
    @Test
    public void testRoom_GetterSetter() {
        final Player player = new Player();
        final Room testRoom = new Room();
        
        player.setCurrentRoom(testRoom);

        assertEquals("Field wasn't retrieved/set properly", testRoom, player.getCurrentRoom());
    }

    @Test
    public void testGetNextDisplay_withMakeMove_PlayerPosition() {
        RogueParser parser = new RogueParser("src/test/java/testFileLocations.json");
        String room = "----+---------------\n|..................|\n|@.................|\n|..................|\n|..................|\n|..................|\n|..................|\n+..................|\n|..................|\n-+------------------";
        int found = 0;

        final Rogue rogue = new Rogue(parser);
        
        ArrayList<Room> allRooms = rogue.getRooms();
        Room testingRoom = new Room();

        for (Room testRoom : allRooms) {
            if (testRoom.isPlayerInRoom()) {
                testingRoom = testRoom;
                found = 1;
            }
        }

        if(found == 0) {
            assertTrue("Unable to find room containing player", false);
            return;
        }

        Player player = testingRoom.getPlayer();

        if(player == null) {
            assertTrue("Unable to find player data from room", false);
            return;
        }

        //get initial xy location
        Point initialPosition = player.getXyLocation();
        Point expectedPosition = new Point(initialPosition.x, initialPosition.y + 1);

        try {
            //Make a move
            rogue.makeMove(rogue.DOWN);

            //Get updated xy location
            Point testPoint = player.getXyLocation();

            //Check if the point updated
            assertEquals("Invalid player position", testPoint, expectedPosition);

        } catch (InvalidMoveException e) {
            assertTrue("Caught an InvalidMoveException", false);
        } catch (Exception e) {
            assertTrue("Caught an exception", false);
        }
    }
}
