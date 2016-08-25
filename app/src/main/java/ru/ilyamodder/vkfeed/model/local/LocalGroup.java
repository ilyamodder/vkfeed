package ru.ilyamodder.vkfeed.model.local;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

/**
 * Created by ilya on 25.08.16.
 */
@ObjectMappable
public class LocalGroup {
    public static final String TABLE_NAME = "groups";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PHOTO_URL = "photo_url";

    @Column(COL_ID)
    long mId;
    @Column(COL_NAME)
    String mName;
    @Column(COL_PHOTO_URL)
    String mPhotoUrl;

    public LocalGroup() {
    }

    public LocalGroup(long id, String name, String photoUrl) {
        mId = id;
        mName = name;
        mPhotoUrl = photoUrl;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
}
