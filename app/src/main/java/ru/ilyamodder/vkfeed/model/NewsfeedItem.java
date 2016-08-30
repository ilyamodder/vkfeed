package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ilya on 21.08.16.
 */

public class NewsfeedItem {
    @SerializedName("attachments")
    List<Attachment> mAttachments;
    @SerializedName("post_id")
    private long mPostId;
    @SerializedName("source_id")
    private long mSourceId;
    @SerializedName("date")
    private long mDate;
    @SerializedName("text")
    private String mText;
    @SerializedName("likes")
    private Likes mLikes;

    public NewsfeedItem(long postId, long sourceId, long date, String text, int likesCount,
                        List<Attachment> attachments) {
        mPostId = postId;
        mSourceId = sourceId;
        mDate = date;
        mText = text;
        mLikes = new Likes(likesCount);
        mAttachments = attachments;
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

    public int getLikesCount() {
        return mLikes == null ? 0 : mLikes.getCount();
    }

    public List<Attachment> getAttachments() {
        return mAttachments;
    }

    private static class Likes {
        @SerializedName("count")
        int mCount;

        public Likes() {
        }

        Likes(int count) {
            mCount = count;
        }

        int getCount() {
            return mCount;
        }
    }
}
