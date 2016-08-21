package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya on 21.08.16.
 */

public class Newsfeed {
    @SerializedName("items")
    private List<NewsfeedItem> mItems;
    @SerializedName("profile")
    private List<Profile> mProfiles;
    @SerializedName("groups")
    private List<Group> mGroups;

    @SerializedName("next_from")
    private String mNextFrom;

    public Newsfeed(List<NewsfeedItem> items, List<Profile> profiles, List<Group> groups,
                    String nextFrom) {
        mItems = items;
        mProfiles = profiles;
        mGroups = groups;
        mNextFrom = nextFrom;
    }

    public List<NewsfeedItem> getItems() {
        return mItems;
    }

    public List<Profile> getProfiles() {
        return mProfiles;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public String getNextFrom() {
        return mNextFrom;
    }
}
