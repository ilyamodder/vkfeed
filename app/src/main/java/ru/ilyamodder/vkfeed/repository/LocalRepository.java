package ru.ilyamodder.vkfeed.repository;

import java.util.List;

import ru.ilyamodder.vkfeed.model.Newsfeed;
import ru.ilyamodder.vkfeed.model.VKResponse;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import rx.Observable;

/**
 * Created by ilya on 20.08.16.
 */

public interface LocalRepository {
    Observable<List<JoinedPost>> getNewsfeed(int offset, int count);

    Observable<JoinedPost> getPost(long id, long sourceId);

    void insertPosts(VKResponse<Newsfeed> newsfeed);

    String getNextOffset();

    void setNextOffset(String nextOffset);

    void clearAll();
}
