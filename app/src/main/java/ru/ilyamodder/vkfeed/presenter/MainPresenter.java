package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKSdk;

import java.util.ArrayList;

import me.tatarka.rxloader.RxLoader1;
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
import rx.schedulers.Schedulers;

/**
 * Created by ilya on 24.08.16.
 */

public class MainPresenter {
    public static final int ROWS_PER_PAGE = 20;
    public static final String LOADER_NETWORK = "network";
    private static final String NEXT_OFFSET =
            "ru.ilyamodder.vkfeed.presenter.MainPresenter.NEXT_OFFSET";
    private static final String POSTS =
            "ru.ilyamodder.vkfeed.presenter.MainPresenter.POSTS";
    private Activity mActivity;
    private MainView mView;
    private RxLoaderManager mRxLoaderManager;
    private String mNextOffset;
    private ArrayList<JoinedPost> mPosts;

    private RxLoader1<String, JoinedPostsResponse> mNetworkLoader;

    public MainPresenter(Activity activity, MainView view) {
        mActivity = activity;
        mView = view;
        mRxLoaderManager = RxLoaderManager.get(mActivity);
        mNextOffset = "";
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
                        if (mNextOffset.isEmpty()) {
                            mPosts.clear();
                        }
                        if (!mNextOffset.equals(value.getNextFrom())) {
                            mPosts.addAll(value.getPosts());
                        }
                        mView.showFeed(mPosts);
                        mNextOffset = value.getNextFrom();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showError();
                        mView.hideLoading();
                    }
                });
    }

    public void onActivityCreate(Bundle savedInstanceState) {
        if (!VKSdk.isLoggedIn()) {
            mView.showLoginActivity();
            return;
        }

        if (savedInstanceState != null) {
            mNextOffset = savedInstanceState.getString(NEXT_OFFSET);
            mPosts = savedInstanceState.getParcelableArrayList(POSTS);
        }

        createLoaders();
        mNetworkLoader.start(mNextOffset);

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(NEXT_OFFSET, mNextOffset);
        outState.putParcelableArrayList(POSTS, mPosts);
    }

    public void loadMore() {
        mNetworkLoader.restart(mNextOffset);
    }

    public void onItemClick(long id) {
        Log.d("debug", id + " clicked");
    }


    public void refresh() {
        mNextOffset = "";
        loadMore();
    }
}
