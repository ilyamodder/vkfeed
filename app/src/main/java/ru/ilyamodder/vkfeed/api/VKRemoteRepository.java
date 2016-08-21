package ru.ilyamodder.vkfeed.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.model.VKApiPost;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.ilyamodder.vkfeed.BuildConfig;
import ru.ilyamodder.vkfeed.repository.RemoteRepository;
import rx.Observer;

/**
 * Created by ilya on 20.08.16.
 */

public class VKRemoteRepository implements RemoteRepository {
    private Context mContext;
    private Retrofit mRetrofit;

    public VKRemoteRepository(Context mContext) {
        this.mContext = mContext;
        mRetrofit = createRetrofit();
    }

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createClient())
                .build();
    }

    private OkHttpClient createClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new ApiVersionInterceptor())
                .authenticator(new VkAuthenticator())
                .build();
    }

    @Override
    public void login(Activity activity, String[] scope) {
        VKSdk.login(activity, scope);
    }

    @Override
    public boolean processActivityResult(int requestCode, int resultCode, Intent data, Observer<String> observer) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                observer.onNext(res.accessToken);
                observer.onCompleted();
            }

            @Override
            public void onError(VKError error) {
                //TODO wrap the VKError
                observer.onError(new IOException());
            }
        });
    }

    public void getNewsfeed() {

    }
}