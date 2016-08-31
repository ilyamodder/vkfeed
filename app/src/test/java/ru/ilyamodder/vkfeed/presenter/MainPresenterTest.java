package ru.ilyamodder.vkfeed.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader1;
import me.tatarka.rxloader.RxLoader2;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.model.local.JoinedPostsResponse;
import ru.ilyamodder.vkfeed.repository.LocalRepository;
import ru.ilyamodder.vkfeed.repository.RemoteRepository;
import ru.ilyamodder.vkfeed.view.MainView;
import rx.functions.Func1;
import rx.functions.Func2;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by ilya on 31.08.16.
 */
public class MainPresenterTest {
    MainView mView;
    MainPresenter mPresenter;
    RxLoaderManager mRxLoaderManager;
    LocalRepository mLocalRepository;
    RemoteRepository mRemoteRepository;
    RxLoader1<String, JoinedPostsResponse> mNetworkLoader;
    RxLoader2<Integer, Integer, List<JoinedPost>> mCacheLoader;

    RxLoaderObserver<JoinedPostsResponse> mNetworkObserver;
    RxLoaderObserver<List<JoinedPost>> mCacheObserver;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mView = mock(MainView.class);
        mRxLoaderManager = mock(RxLoaderManager.class);
        mPresenter = new MainPresenter(mView, mRxLoaderManager);

        mLocalRepository = mock(LocalRepository.class);
        mRemoteRepository = mock(RemoteRepository.class);

        mNetworkLoader = mock(RxLoader1.class);
        mCacheLoader = mock(RxLoader2.class);

        when(mRxLoaderManager.create(eq(MainPresenter.LOADER_CACHE),
                any(Func2.class), any(RxLoaderObserver.class))).thenAnswer(invocation -> {
            mCacheObserver = (RxLoaderObserver<List<JoinedPost>>) invocation.getArguments()[2];
            return mCacheLoader;
        });
        when(mRxLoaderManager.create(eq(MainPresenter.LOADER_NETWORK),
                any(Func1.class), any(RxLoaderObserver.class))).thenAnswer(invocation -> {
            mNetworkObserver = (RxLoaderObserver<JoinedPostsResponse>)
                    invocation.getArguments()[2];
            return mNetworkLoader;
        });

        RepositoryProvider.setLocalRepository(mLocalRepository);
        RepositoryProvider.setRemoteRepository(mRemoteRepository);
    }

    @Test
    public void testNotLoggedIn() throws Exception {
        when(mLocalRepository.isLoggedIn()).thenReturn(false);

        mPresenter.onActivityCreate(null);
        verify(mView).showLoginActivity();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testLoadOnePageFromCache() throws Exception {
        when(mLocalRepository.isLoggedIn()).thenReturn(true);

        mPresenter.onActivityCreate(null);

        List<JoinedPost> posts = new ArrayList<>();
        posts.add(new JoinedPost(1213, -13124234, "name", new Date(), "http://ya.ru/",
                "text", null, 200));

        mCacheObserver.onStarted();
        verify(mView).showLoading();

        mCacheObserver.onNext(posts);
        verify(mView).showFeed(eq(posts));
        verify(mView).hideLoading();

    }
}