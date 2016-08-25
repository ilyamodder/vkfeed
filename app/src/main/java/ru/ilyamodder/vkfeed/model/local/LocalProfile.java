package ru.ilyamodder.vkfeed.model.local;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

/**
 * Created by ilya on 25.08.16.
 */
@ObjectMappable
public class LocalProfile {
    public static final String TABLE_NAME = "profiles";
    public static final String COL_ID = "_id";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_PHOTO_URL = "photo_url";

    @Column(COL_ID)
    long mId;
    @Column(COL_FIRST_NAME)
    String mFirstName;
    @Column(COL_LAST_NAME)
    String mLastName;
    @Column(COL_PHOTO_URL)
    String mPhotoUrl;

    public LocalProfile() {
    }

    public LocalProfile(long id, String firstName, String lastName, String photoUrl) {
        mId = id;
        mFirstName = firstName;
        mLastName = lastName;
        mPhotoUrl = photoUrl;
    }

    public long getId() {
        return mId;
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
