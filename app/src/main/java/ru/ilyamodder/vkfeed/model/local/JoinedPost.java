package ru.ilyamodder.vkfeed.model.local;

import com.hannesdorfmann.sqlbrite.objectmapper.annotation.Column;
import com.hannesdorfmann.sqlbrite.objectmapper.annotation.ObjectMappable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ilya on 25.08.16.
 */
@ObjectMappable
public class JoinedPost {
    @Column("_id")
    long mId;
    @Column("name")
    String mName;
    @Column("date")
    Date mDate;
    @Column("avatar")
    String mAvatar;
    @Column("text")
    String mText;
    List<String> mPhotos;
    @Column("likes_count")
    int mLikesCount;

    public JoinedPost() {
    }

    public JoinedPost(long id, String name, Date date, String avatar, String text,
                      List<String> photos, int likesCount) {
        mId = id;
        mName = name;
        mDate = date;
        mAvatar = avatar;
        mText = text;
        mPhotos = photos;
        mLikesCount = likesCount;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public Date getDate() {
        return mDate;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public String getText() {
        return mText;
    }

    @Column("photos")
    public void setRawPhotos(String rawPhotos) {
        mPhotos = new ArrayList<>(Arrays.asList(rawPhotos.split(",")));
    }

    public List<String> getPhotos() {
        return mPhotos;
    }
}
