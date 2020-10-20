package com.prj666.recycling_vision.user;

public class Item {
    // Class variables
    private int itemID;
    private String itemName;

    //Constructor
    public Item(int itemID, String itemName) {
        this.itemID = itemID;
        this.itemName = itemName;
    }

    // Getters and setters

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
