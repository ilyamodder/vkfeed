package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 25.08.16.
 */

public class Attachment {
    @SerializedName("type")
    private String mType;
    @SerializedName("photo")
    private Photo mPhoto;

    public Attachment() {
    }

    public Attachment(String type, Photo photo) {
        mType = type;
        mPhoto = photo;
    }

    public String getType() {
        return mType;
    }

    public Photo getPhoto() {
        return mPhoto;
    }
}
