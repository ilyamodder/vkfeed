package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.view.PostView;
import rx.schedulers.Schedulers;

/**
 * Created by ilya on 30.08.16.
 */

public class PostPresenter {
    private PostView mView;
    private Activity mActivity;
    private RxLoaderManager mLoaderManager;
    private long mPostId;
    private long mSourceId;

    public PostPresenter(PostView view, Activity activity, long postId, long sourceId) {
        mView = view;
        mActivity = activity;
        mPostId = postId;
        mSourceId = sourceId;
        mLoaderManager = RxLoaderManager.get(activity);
    }

    public void onActivityCreate() {
        mLoaderManager.create(RepositoryProvider.getLocalRepository().getPost(mPostId, mSourceId)
                        .subscribeOn(Schedulers.io()),
                new RxLoaderObserver<JoinedPost>() {
                    @Override
                    public void onNext(JoinedPost value) {
                        mView.showPost(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }).start();
    }
}
