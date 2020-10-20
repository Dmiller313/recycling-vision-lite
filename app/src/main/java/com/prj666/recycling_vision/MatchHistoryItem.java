package com.prj666.recycling_vision;

import android.graphics.Bitmap;

import java.util.Calendar;
import java.util.Date;

public class MatchHistoryItem {
    //IDs
    private int historyItemID;
    private int objectID;
    private int userID;

    //Data
    private String foundRecyclingInstruction;
    private String objectName;
    private Date matchDateTime;
    private Bitmap objectImageBitmap;
    private byte[] objectImageBytes;
    private double probabilityMatch;

    //Constructor for a new history item
    public MatchHistoryItem(int historyItemID, int objectID, int userID) {
        if (historyItemID >=0 && objectID >=0 && userID >=0)
        {
            this.historyItemID = historyItemID;
            this.objectID = objectID;
            this.userID = userID;
        }

        this.foundRecyclingInstruction = null;
        this.matchDateTime = Calendar.getInstance().getTime();//Set current time
    }

    //Constructor for an existing history item
    public MatchHistoryItem(int historyItemID, int objectID, int userID, String foundRecyclingInstruction, Date matchDateTime) {

        if (historyItemID >=0 && objectID >=0 && userID >=0 && foundRecyclingInstruction != null && matchDateTime != null)
        {
            this.historyItemID = historyItemID;
            this.objectID = objectID;
            this.userID = userID;

            this.foundRecyclingInstruction = foundRecyclingInstruction;
            this.matchDateTime = matchDateTime;
        }
    }

    //setters and getters to access specific object data
    public void setID(int id)
    {
        this.objectID = id;
    }

    public int getObjectID()
    {
        return this.objectID;
    }

    public void setObjectName(String name)
    {
        if(name!=null)
        {
            this.objectName = name;
        }
    }

    public String getObjectName()
    {
        return this.objectName;
    }

    public void setProbabilityMatch(double percentage)
    {
        if(percentage > 0.0)
        {
            this.probabilityMatch = percentage;
        }
    }

    public double getProbabilityMatch()
    {
        return this.probabilityMatch;
    }

    public void setObjectImageBytes(byte[] imageBytes)
    {
        if(imageBytes!=null)
        {
            this.objectImageBytes = new byte[imageBytes.length];
            this.objectImageBytes = imageBytes;
        }
    }

    public byte[] getObjectImageBytes()
    {
        return this.objectImageBytes;
    }

    public void setObjectImageBitmap(Bitmap image)
    {
        if(image!=null)
        {
            this.objectImageBitmap = image;
        }
    }

    public Bitmap getObjectImageBitmap()
    {
        return this.objectImageBitmap;
    }

    public String getFoundRecyclingInstruction()
    {
        return this.foundRecyclingInstruction;
    }

    public Date getMatchDateTime()
    {
        return this.matchDateTime;
    }

    public int getUserID()
    {
        return this.userID;
    }

    public int getHistoryItemID()
    {
        return this.historyItemID;
    }
}
