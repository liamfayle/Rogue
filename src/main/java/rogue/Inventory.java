package rogue;

import java.io.Serializable;
import java.util.ArrayList;




public class Inventory implements Serializable {
    private ArrayList<Item> inv;
    private ArrayList<Food> food;
    private ArrayList<SmallFood> smallFood;
    private ArrayList<Clothing> clothing;
    private ArrayList<Potion> potion;
    private ArrayList<Ring> ring;
    private ArrayList<Item> general;


    /**
     * Default constructor for inventory.
     */
    public Inventory() {
        inv = new ArrayList();
        food = new ArrayList();
        smallFood = new ArrayList();
        clothing = new ArrayList();
        potion = new ArrayList();
        ring = new ArrayList();
        general = new ArrayList();
    }

    /**
     * add item to inventory.
     * @param i item to be added
     */
    public void addItem(Item i) {
        inv.add(i);
        classifyItem(i);
    }


    private void classifyItem(Item i) {
        if (i.getType().equals("Food")) {
            food.add(new Food(i));
        } else if (i.getType().equals("SmallFood")) {
            smallFood.add(new SmallFood(i));
        } else if (i.getType().equals("Clothing")) {
            clothing.add(new Clothing(i));
        } else if (i.getType().equals("Potion")) {
            potion.add(new Potion(i));
        } else if (i.getType().equals("Ring")) {
            ring.add(new Ring(i));
        } else {
            general.add(i);
        }
    }

    /**
     * get arraylist of items in inv.
     * @return inventory
     */
    public ArrayList<Item> getItems() {
        return inv;
    }


    /**
     * Gets food items.
     * @return food
     */
    public ArrayList<Food> getFood() {
        return food;
    }

    /**
     * Gets smallfood items.
     * @return smallfood
     */
    public ArrayList<SmallFood> getSmallFood() {
        return smallFood;
    }

    /**
     * get clothing items.
     * @return clothing
     */
    public ArrayList<Clothing> getClothing() {
        return clothing;
    }


    /**
     * get potion items.
     * @return potion
     */
    public ArrayList<Potion> getPotion() {
        return potion;
    }

    /**
     * get ring items.
     * @return ring
     */
    public ArrayList<Ring> getRing() {
        return ring;
    }


    /**
     * Removes item from inv.
     * @param item item to remove
     */
    public void removeItem(Item item) {
        int id = item.getId();
        String type = item.getType();
        removeInv(id);
        if (item.getType().equals("Food")) {
            removeFood(id);
        } else if (item.getType().equals("SmallFood")) {
            removeSmallFood(id);
        } else if (item.getType().equals("Clothing")) {
            removeClothing(id);
        } else if (item.getType().equals("Potion")) {
            removePotion(id);
        } else if (item.getType().equals("Ring")) {
            removeRing(id);
        } else {
            removeGen(id);
        }
    }


    private void removeInv(int id) {
        for (int i = 0; i < inv.size(); i++) {
            if (inv.get(i).getId() == id) {
                inv.remove(i);
                return;
            }
        }
    }

    private void removeFood(int id) {
        for (int i = 0; i < food.size(); i++) {
            if (food.get(i).getId() == id) {
                food.remove(i);
                return;
            }
        }
    }

    private void removeSmallFood(int id) {
        for (int i = 0; i < smallFood.size(); i++) {
            if (smallFood.get(i).getId() == id) {
                smallFood.remove(i);
                return;
            }
        }
    }

    private void removeClothing(int id) {
        for (int i = 0; i < clothing.size(); i++) {
            if (clothing.get(i).getId() == id) {
                clothing.remove(i);
                return;
            }
        }
    }

    private void removePotion(int id) {
        for (int i = 0; i < potion.size(); i++) {
            if (potion.get(i).getId() == id) {
                potion.remove(i);
                return;
            }
        }
    }


    private void removeRing(int id) {
        for (int i = 0; i < ring.size(); i++) {
            if (ring.get(i).getId() == id) {
                ring.remove(i);
                return;
            }
        }
    }

    private void removeGen(int id) {
        for (int i = 0; i < general.size(); i++) {
            if (general.get(i).getId() == id) {
                general.remove(i);
                return;
            }
        }
    }





}
