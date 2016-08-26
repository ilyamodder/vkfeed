package ru.ilyamodder.vkfeed.model.local;

import java.util.List;

/**
 * Created by ilya on 26.08.16.
 */

public class JoinedPostsResponse {
    private List<JoinedPost> mPosts;
    private String nextFrom;

    public JoinedPostsResponse(List<JoinedPost> posts, String nextFrom) {
        mPosts = posts;
        this.nextFrom = nextFrom;
    }

    public List<JoinedPost> getPosts() {
        return mPosts;
    }

    public String getNextFrom() {
        return nextFrom;
    }
}
