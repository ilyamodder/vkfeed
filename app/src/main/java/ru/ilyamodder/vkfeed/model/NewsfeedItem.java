package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 21.08.16.
 */

public class NewsfeedItem {
    @SerializedName("post_id")
    private long mPostId;
    @SerializedName("source_id")
    private long mSourceId;
    @SerializedName("date")
    private long mDate;
    @SerializedName("text")
    private String mText;

    public NewsfeedItem(long postId, long sourceId, long date, String text) {
        mPostId = postId;
        mSourceId = sourceId;
        mDate = date;
        mText = text;
    }

    public long getPostId() {
        return mPostId;
    }

    public long getSourceId() {
        return mSourceId;
    }

    public long getDate() {
        return mDate;
    }

    public String getText() {
        return mText;
    }
}
