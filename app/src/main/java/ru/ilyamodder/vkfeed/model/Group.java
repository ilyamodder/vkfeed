package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 21.08.16.
 */

public class Group {
    @SerializedName("gid")
    private long mGroupId;
    @SerializedName("name")
    private String mName;
    @SerializedName("photo")
    private String mPhotoUrl;

    public Group(long groupId, String name, String photoUrl) {
        mGroupId = groupId;
        mName = name;
        mPhotoUrl = photoUrl;
    }

    public long getGroupId() {
        return mGroupId;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
