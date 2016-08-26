package ru.ilyamodder.vkfeed.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.ilyamodder.vkfeed.model.Newsfeed;
import ru.ilyamodder.vkfeed.model.VKResponse;
import rx.Observable;

/**
 * Created by ilya on 20.08.16.
 */

public interface VKService {
    @GET("newsfeed.get?filters=post")
    Observable<VKResponse<Newsfeed>> getNewsfeed(@Query("start_from") String startFrom,
                                                 @Query("count") int count);
}
