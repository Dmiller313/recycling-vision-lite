package com.prj666.recycling_vision;

import android.graphics.Bitmap;

public class IdentifiedObject
{
    private int objectID;
    private String objectName;
    private double probabilityMatch;
    private byte[] objectImageBytes;
    private Bitmap objectImageBitmap;

    public IdentifiedObject()
    {
        this.objectID=0;
        this.objectName=null;
        this.probabilityMatch=0.0;
        this.objectImageBytes=null;
        this.objectImageBitmap=null;
    }

    public IdentifiedObject(int id, String name, double percentage, byte[] imageBytes)
    {
        if(name!=null && percentage>0.0 && imageBytes!=null)
        {
            this.objectName = name;
            this.probabilityMatch = percentage;
            this.objectImageBytes = new byte[imageBytes.length];
            this.objectImageBytes = imageBytes;
        }
    }

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
}