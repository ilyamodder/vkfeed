package ru.ilyamodder.vkfeed.api;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ilya on 21.08.16.
 */

public class VkTokenAdderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (VKSdk.isLoggedIn()) {
            HttpUrl url = request.url().newBuilder()
                    .addQueryParameter("access_token", VKAccessToken.currentToken().accessToken)
                    .build();
            request = request.newBuilder().url(url).build();
        }
        return chain.proceed(request);
    }
}
