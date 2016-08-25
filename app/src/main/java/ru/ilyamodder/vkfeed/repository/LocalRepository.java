package ru.ilyamodder.vkfeed.repository;

import java.util.List;

import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import rx.Observable;

/**
 * Created by ilya on 20.08.16.
 */

public interface LocalRepository {
    Observable<List<JoinedPost>> getNewsfeed(int offset, int count);
}
