package rogue;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.util.ArrayList; 
import org.junit.*;

import rogue.*;


import java.awt.Point;

public class ItemClassTests {

    /**
     * Test the Id getter and setter
     * Expected: Id that was passed into the setter
     */
    @Test
    public void testId_GetterSetter() {
        int testId = 1;

        final Item item = new Item();
        item.setId(testId);

        assertEquals("Field wasn't retrieved/set properly", testId, item.getId());
    }

    /**
     * Test the Name getter and setter
     * Expected: Name that was passed into the setter
     */
    @Test
    public void testName_GetterSetter() {
        String testName = "Mana Potion";

        final Item item = new Item();
        item.setName(testName);

        assertEquals("Field wasn't retrieved/set properly", testName, item.getName());
    }

    /**
     * Test the Type getter and setter
     * Expected: Type that was passed into the setter
     */
    @Test
    public void testType_GetterSetter() {
        String testType = "Consumable";

        final Item item = new Item();
        item.setType(testType);

        assertEquals("Field wasn't retrieved/set properly", testType, item.getType());
    }

    /**
     * Test the DisplayCharacter getter and setter
     * Expected: DisplayCharacter that was passed into the setter
     */
    @Test
    public void testDisplayCharacter_GetterSetter() {
        Character testDisplayCharacter = '8';

        final Item item = new Item();
        item.setDisplayCharacter(testDisplayCharacter);

        assertEquals("Field wasn't retrieved/set properly", testDisplayCharacter, item.getDisplayCharacter());
    }

    /**
     * Test the Description getter and setter
     * Expected: Description that was passed into the setter
     */
    @Test
    public void testDescription_GetterSetter() {
        String testDescription = "This item will heal you by 8 points";

        final Item item = new Item();
        item.setDescription(testDescription);

        assertEquals("Field wasn't retrieved/set properly", testDescription, item.getDescription());
    }

    /**
     * Test the XYLocation getter and setter
     * Expected: Point that was passed into the setter
     */
    @Test
    public void testXYLocation_GetterSetter() {
        Point testPoint = new Point(12,5);

        final Item item = new Item();
        item.setXyLocation(testPoint);

        assertEquals("Field wasn't retrieved/set properly", testPoint, item.getXyLocation());
    }

    /**
     * Test the CurrentRoom getter and setter
     * Expected: Room that was passed into the setter
     */
    @Test
    public void testCurrentRoom_GetterSetter() {
        Room testRoom = new Room();

        final Item item = new Item();
        item.setCurrentRoom(testRoom);

        assertEquals("Field wasn't retrieved/set properly", testRoom, item.getCurrentRoom());
    }
}
