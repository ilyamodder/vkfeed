package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKSdk;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader1;
import me.tatarka.rxloader.RxLoader2;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.model.local.JoinedPostsResponse;
import ru.ilyamodder.vkfeed.rx.Converters;
import ru.ilyamodder.vkfeed.view.MainView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by ilya on 24.08.16.
 */

public class MainPresenter {
    public static final int ROWS_PER_PAGE = 20;
    public static final String LOADER_NETWORK = "network";
    public static final String LOADER_CACHE = "cache";
    private static final String POSTS =
            "ru.ilyamodder.vkfeed.presenter.MainPresenter.POSTS";
    private static final String PAGES_LOADED =
            "ru.ilyamodder.vkfeed.presenter.MainPresenter.PAGES_LOADED";
    private Activity mActivity;
    private MainView mView;
    private RxLoaderManager mRxLoaderManager;
    private String mNextOffset;
    private int mPagesLoaded;
    private ArrayList<JoinedPost> mPosts;

    private RxLoader1<String, JoinedPostsResponse> mNetworkLoader;
    private RxLoader2<Integer, Integer, List<JoinedPost>> mCacheLoader;

    public MainPresenter(Activity activity, MainView view) {
        mActivity = activity;
        mView = view;
        mRxLoaderManager = RxLoaderManager.get(mActivity);
        mNextOffset = "";
        mPagesLoaded = 0;
        mPosts = new ArrayList<>();
    }

    private void createLoaders() {
        mNetworkLoader = mRxLoaderManager.create(LOADER_NETWORK,
                new Func1<String, Observable<JoinedPostsResponse>>() {
                    @Override
                    public Observable<JoinedPostsResponse> call(String s) {
                        return RepositoryProvider.getRemoteRepository()
                                .getNewsfeed(s, ROWS_PER_PAGE)
                                .doOnNext(RepositoryProvider.getLocalRepository()::insertPosts)
                                .flatMap(Converters::toJoinedPost)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                },
                new RxLoaderObserver<JoinedPostsResponse>() {
                    @Override
                    public void onStarted() {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(JoinedPostsResponse value) {
                        if (mPagesLoaded == 0) {
                            mPosts.clear();
                        }
                        if (!mNextOffset.equals(value.getNextFrom())) {
                            mPosts.addAll(value.getPosts());
                            mPagesLoaded++;
                        }
                        mView.showFeed(mPosts);
                        mNextOffset = value.getNextFrom();
                        saveNextOffset();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showError();
                        mView.hideLoading();
                    }
                });
        mCacheLoader = mRxLoaderManager.create(LOADER_CACHE, new Func2<Integer, Integer, Observable<List<JoinedPost>>>() {
            @Override
            public Observable<List<JoinedPost>> call(Integer offset, Integer count) {
                return RepositoryProvider.getLocalRepository()
                        .getNewsfeed(offset, count)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .first();
            }
        }, new RxLoaderObserver<List<JoinedPost>>() {
            @Override
            public void onStarted() {
                mView.showLoading();
            }

            @Override
            public void onNext(List<JoinedPost> value) {
                if (value.isEmpty()) {
                    mNetworkLoader.restart(mNextOffset);
                } else {
                    if (mPosts.size() <= ROWS_PER_PAGE * mPagesLoaded) {
                        mPosts.addAll(value);
                        mPagesLoaded++;
                    }
                    mView.showFeed(mPosts);
                    mView.hideLoading();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onCompleted() {
                Log.d("cache loader", "complete");
            }
        });
    }

    public void onActivityCreate(Bundle savedInstanceState) {
        if (!VKSdk.isLoggedIn()) {
            mView.showLoginActivity();
            return;
        }

        mNextOffset = RepositoryProvider.getLocalRepository().getNextOffset();

        if (savedInstanceState != null) {
            mPosts = savedInstanceState.getParcelableArrayList(POSTS);
            mPagesLoaded = savedInstanceState.getInt(PAGES_LOADED);
        }

        createLoaders();
        mCacheLoader.start(ROWS_PER_PAGE * mPagesLoaded, ROWS_PER_PAGE);

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(POSTS, mPosts);
        outState.putInt(PAGES_LOADED, mPagesLoaded);
        saveNextOffset();
    }

    public void loadMore() {
        Log.d("loadmore", "");
        mCacheLoader.restart(ROWS_PER_PAGE * mPagesLoaded, ROWS_PER_PAGE);
    }

    public void onItemClick(long id, long srcId) {
        mView.showPostActivity(id, srcId);
    }

    public void refresh() {
        mNextOffset = "";
        mPagesLoaded = 0;
        RepositoryProvider.getLocalRepository().clearAll();
        mNetworkLoader.restart(mNextOffset);
        saveNextOffset();
    }

    private void saveNextOffset() {
        RepositoryProvider.getLocalRepository().setNextOffset(mNextOffset);
    }
}
