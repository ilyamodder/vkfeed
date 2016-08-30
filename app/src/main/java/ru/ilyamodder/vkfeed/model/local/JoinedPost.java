package ru.ilyamodder.vkfeed.model.local;

import android.os.Parcel;
import android.os.Parcelable;

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
public class JoinedPost implements Parcelable {
    public static final Creator<JoinedPost> CREATOR = new Creator<JoinedPost>() {
        @Override
        public JoinedPost createFromParcel(Parcel in) {
            return new JoinedPost(in);
        }

        @Override
        public JoinedPost[] newArray(int size) {
            return new JoinedPost[size];
        }
    };
    @Column("_id")
    long mId;
    @Column("source_id")
    long mSrcId;
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

    public JoinedPost(long id, long srcId, String name, Date date, String avatar, String text,
                      List<String> photos, int likesCount) {
        mId = id;
        mSrcId = srcId;
        mName = name;
        mDate = date;
        mAvatar = avatar;
        mText = text;
        mPhotos = photos;
        mLikesCount = likesCount;
    }

    protected JoinedPost(Parcel in) {
        mId = in.readLong();
        mSrcId = in.readLong();
        mName = in.readString();
        mAvatar = in.readString();
        mText = in.readString();
        mPhotos = in.createStringArrayList();
        mLikesCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mSrcId);
        dest.writeString(mName);
        dest.writeString(mAvatar);
        dest.writeString(mText);
        dest.writeStringList(mPhotos);
        dest.writeInt(mLikesCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return mId;
    }

    public long getSrcId() {
        return mSrcId;
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

    @Column(value = "photos", throwOnColumnIndexNotFound = false)
    public void setRawPhotos(String rawPhotos) {
        if (rawPhotos != null) {
            mPhotos = new ArrayList<>(Arrays.asList(rawPhotos.split(",")));
        } else {
            mPhotos = new ArrayList<>();
        }
    }

    public List<String> getPhotos() {
        return mPhotos;
    }

    public int getLikesCount() {
        return mLikesCount;
    }
}
