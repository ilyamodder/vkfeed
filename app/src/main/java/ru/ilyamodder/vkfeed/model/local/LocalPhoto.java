package ru.ilyamodder.vkfeed.model.local;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

/**
 * Created by ilya on 25.08.16.
 */
@ObjectMappable
public class LocalPhoto {
    public static final String TABLE_NAME = "photos";
    public static final String COL_ID = "_id";
    public static final String COL_POST_ID = "post_id";
    public static final String COL_POST_SRC_ID = "post_id";
    public static final String COL_PHOTO_75 = "photo_75";
    public static final String COL_PHOTO_130 = "photo_130";
    public static final String COL_PHOTO_604 = "photo_604";

    @Column(COL_ID)
    long mId;
    @Column(COL_POST_ID)
    long mPostId;
    @Column(COL_POST_SRC_ID)
    long mPostSrcId;
    @Column(COL_PHOTO_75)
    String mPhoto75;
    @Column(COL_PHOTO_130)
    String mPhoto130;
    @Column(COL_PHOTO_604)
    String mPhoto604;

    public LocalPhoto() {
    }

    public LocalPhoto(long id, long postId, long postSrcId, String photo75, String photo130, String photo604) {
        mId = id;
        mPostId = postId;
        mPostSrcId = mPostSrcId;
        mPhoto75 = photo75;
        mPhoto130 = photo130;
        mPhoto604 = photo604;
    }

    public long getId() {
        return mId;
    }

    public long getPostId() {
        return mPostId;
    }

    public long getPostSrcId() {
        return mPostSrcId;
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
