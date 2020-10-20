package com.prj666.recycling_vision;

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
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}