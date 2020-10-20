package com.prj666.recycling_vision.user;

public class RecyclingMessage {
    // Class variables
    private int messageID;
    private String message;

    //Constructor
    public RecyclingMessage(int messageID, String message) {
        this.messageID = messageID;
        this.message = message;
    }

    // Getters and setters
    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
