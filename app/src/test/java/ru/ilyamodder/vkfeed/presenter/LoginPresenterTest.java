package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.repository.RemoteRepository;
import ru.ilyamodder.vkfeed.view.LoginView;
import rx.Observer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by ilya on 31.08.16.
 */
public class LoginPresenterTest {
    public static final String[] SCOPE = {"test", "testtest", "ololo"};

    LoginView mView;
    Activity mActivity;
    LoginPresenter mPresenter;

    Resources mResources;

    @Before
    public void setUp() throws Exception {
        mView = mock(LoginView.class);
        mActivity = mock(Activity.class);
        mPresenter = new LoginPresenter(mActivity, mView);
        mResources = mock(Resources.class);

        when(mActivity.getResources()).thenReturn(mResources);
        when(mResources.getStringArray(eq(R.array.vk_app_scope))).thenReturn(SCOPE);
    }

    @Test
    public void testSuccessLogin() throws Exception {
        RemoteRepository successRepository = mock(RemoteRepository.class);
        doNothing().when(successRepository).login(eq(mActivity), eq(SCOPE));
        when(successRepository.processActivityResult(eq(0), eq(0), eq(null),
                anyObject())).thenAnswer(invocation -> {
            //noinspection unchecked
            Observer<String> observer = (Observer<String>) invocation.getArguments()[3];
            observer.onNext("testetyrgeh");
            observer.onCompleted();
            return true;
        });

        RepositoryProvider.setRemoteRepository(successRepository);

        mPresenter.onLoginButtonClicked();
        verify(RepositoryProvider.getRemoteRepository()).login(eq(mActivity), eq(SCOPE));

        assertTrue(mPresenter.onActivityResult(0, 0, null));

        verify(successRepository)
                .processActivityResult(eq(0), eq(0), eq(null), anyObject());
        verify(mView).showMainActivity();
    }

    @Test
    public void testErrorLogin() throws Exception {
        RemoteRepository errorRepository = mock(RemoteRepository.class);
        doNothing().when(errorRepository).login(eq(mActivity), eq(SCOPE));
        when(errorRepository.processActivityResult(eq(0), eq(0), eq(null),
                anyObject())).thenAnswer(invocation -> {
            //noinspection unchecked
            Observer<String> observer = (Observer<String>) invocation.getArguments()[3];
            observer.onError(new IOException());
            return true;
        });

        RepositoryProvider.setRemoteRepository(errorRepository);

        mPresenter.onLoginButtonClicked();

        assertTrue(mPresenter.onActivityResult(0, 0, null));

        verify(errorRepository)
                .processActivityResult(eq(0), eq(0), eq(null), anyObject());

        verifyZeroInteractions(mView);
    }
}