package com.prj666.recycling_vision;

public class ImagePack {
    //ID
    Integer imageID;

    //Data
    Byte[] image;
    Byte[] stableFrames;

    //Constructor for a new ImagePack without any data
    public ImagePack(Integer imageID) {
        this.imageID = imageID;

        this.image = null;
        this.stableFrames = null;
    }

    //Constructor for an ImagePack with data
    public ImagePack(Integer imageID, Byte[] image, Byte[] stableFrames) {
        this.imageID = imageID;

        this.image = image;
        this.stableFrames = stableFrames;
    }
}