package ru.ilyamodder.vkfeed.model.local;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

import java.util.Date;

/**
 * Created by ilya on 25.08.16.
 */
@ObjectMappable
public class LocalNewsfeedItem {
    public static final String TABLE_NAME = "posts";
    public static final String COL_ID = "_id";
    public static final String COL_SOURCE_ID = "source_id";
    public static final String COL_IS_PROFILE_SRC = "is_profile_src";
    public static final String COL_DATE = "date";
    public static final String COL_TEXT = "text";
    public static final String COL_LIKES_COUNT = "likes_count";

    @Column(COL_ID)
    long mId;
    @Column(COL_SOURCE_ID)
    long mSourceId;
    @Column(COL_IS_PROFILE_SRC)
    boolean mIsProfileSource;
    @Column(COL_DATE)
    Date mDate;
    @Column(COL_TEXT)
    String mText;
    @Column(COL_LIKES_COUNT)
    int mLikesCount;

    public LocalNewsfeedItem() {
    }

    public LocalNewsfeedItem(long id, long sourceId, boolean isProfileSource, Date date,
                             String text, int likesCount) {
        mId = id;
        mSourceId = sourceId;
        mIsProfileSource = isProfileSource;
        mDate = date;
        mText = text;
        mLikesCount = likesCount;
    }

    public long getId() {
        return mId;
    }

    public long getSourceId() {
        return mSourceId;
    }

    public boolean isProfileSource() {
        return mIsProfileSource;
    }

    public Date getDate() {
        return mDate;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public String getText() {
        return mText;
    }
}
