package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKSdk;

import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.rx.Converters;
import ru.ilyamodder.vkfeed.view.MainView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ilya on 24.08.16.
 */

public class MainPresenter {
    public static final int ROWS_PER_PAGE = 20;
    private static final String PAGE_NUMBER =
            "ru.ilyamodder.vkfeed.presenter.MainPresenter.PAGE_NUMBER";
    private Activity mActivity;
    private MainView mView;
    private RxLoaderManager mRxLoaderManager;
    private int mCurrentPageNumber;
    private String mNextOffset;

    public MainPresenter(Activity activity, MainView view) {
        mActivity = activity;
        mView = view;
        mRxLoaderManager = RxLoaderManager.get(mActivity);
        mCurrentPageNumber = 0;
    }

    public void onActivityCreate(Bundle savedInstanceState) {
        if (!VKSdk.isLoggedIn()) {
            mView.showLoginActivity();
            return;
        }

        if (savedInstanceState != null) {
            mCurrentPageNumber = savedInstanceState.getInt(PAGE_NUMBER);
        }

        mRxLoaderManager.create(Converters.toJoinedPost(RepositoryProvider.getRemoteRepository()
                        .getNewsfeed("", ROWS_PER_PAGE))
                        .doOnEach(list -> {

                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                , new RxLoaderObserver<List<JoinedPost>>() {
                    @Override
                    public void onStarted() {
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(List<JoinedPost> value) {
                        mView.showFeed(value);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.showError();
                        mView.hideLoading();
                    }
                }
        ).start();

    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(PAGE_NUMBER, mCurrentPageNumber);
    }

    public void loadMore() {
        Log.d("debug", "loading more");
    }

    public void onItemClick(long id) {
        Log.d("debug", id + " clicked");
    }


}
