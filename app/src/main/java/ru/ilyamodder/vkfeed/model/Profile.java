package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 21.08.16.
 */

public class Profile {
    @SerializedName("id")
    private long mUserId;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("photo_100")
    private String mPhotoUrl;

    public Profile() {
    }

    public Profile(long userId, String firstName, String lastName, String photoUrl) {
        mUserId = userId;
        mFirstName = firstName;
        mLastName = lastName;
        mPhotoUrl = photoUrl;
    }

    public long getUserId() {
        return mUserId;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
