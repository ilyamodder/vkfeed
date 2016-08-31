package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.content.Intent;

import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.api.RepositoryProvider;
import ru.ilyamodder.vkfeed.view.LoginView;
import rx.Observer;

/**
 * Created by ilya on 19.08.16.
 */

public class LoginPresenter {

    private Activity mActivity;
    private LoginView mView;

    public LoginPresenter(Activity mActivity, LoginView mView) {
        this.mActivity = mActivity;
        this.mView = mView;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return RepositoryProvider.getRemoteRepository().processActivityResult(requestCode,
                resultCode, data, new Observer<String>() {
            @Override
            public void onCompleted() {
                mView.showMainActivity();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {

            }
        });
    }

    public void onLoginButtonClicked() {
        RepositoryProvider.getRemoteRepository().login(mActivity,
                mActivity.getResources().getStringArray(R.array.vk_app_scope));
    }
}
