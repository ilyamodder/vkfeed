package ru.ilyamodder.vkfeed.model.local;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ilya on 26.08.16.
 */

public class JoinedPostsResponse implements Parcelable {
    public static final Creator<JoinedPostsResponse> CREATOR = new Creator<JoinedPostsResponse>() {
        @Override
        public JoinedPostsResponse createFromParcel(Parcel in) {
            return new JoinedPostsResponse(in);
        }

        @Override
        public JoinedPostsResponse[] newArray(int size) {
            return new JoinedPostsResponse[size];
        }
    };
    private List<JoinedPost> mPosts;
    private String nextFrom;

    public JoinedPostsResponse(List<JoinedPost> posts, String nextFrom) {
        mPosts = posts;
        this.nextFrom = nextFrom;
    }

    protected JoinedPostsResponse(Parcel in) {
        mPosts = in.createTypedArrayList(JoinedPost.CREATOR);
        nextFrom = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mPosts);
        dest.writeString(nextFrom);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<JoinedPost> getPosts() {
        return mPosts;
    }

    public String getNextFrom() {
        return nextFrom;
    }
}
