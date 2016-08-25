package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 25.08.16.
 */

public class Photo {
    @SerializedName("id")
    private long mId;
    @SerializedName("photo_75")
    private String mPhoto75;
    @SerializedName("photo_130")
    private String mPhoto130;
    @SerializedName("photo_604")
    private String mPhoto604;

    public Photo() {
    }

    public Photo(long id, String photo75, String photo130, String photo604) {
        mId = id;
        mPhoto75 = photo75;
        mPhoto130 = photo130;
        mPhoto604 = photo604;
    }

    public long getId() {
        return mId;
    }

    public String getPhoto75() {
        return mPhoto75;
    }

    public String getPhoto130() {
        return mPhoto130;
    }

    public String getPhoto604() {
        return mPhoto604;
    }
}
