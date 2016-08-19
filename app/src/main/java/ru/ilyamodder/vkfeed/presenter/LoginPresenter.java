package ru.ilyamodder.vkfeed.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import me.tatarka.rxloader.RxLoaderManager;
import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.view.LoginView;

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
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                mView.showMainActivity();
            }

            @Override
            public void onError(VKError error) {
                Log.e("vk login", "error: " + error.toString());
            }
        });
    }

    public void onLoginButtonClicked() {
        VKSdk.login(mActivity, mActivity.getResources().getStringArray(R.array.vk_app_scope));
    }
}
