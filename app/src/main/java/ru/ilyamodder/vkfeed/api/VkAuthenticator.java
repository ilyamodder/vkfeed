package ru.ilyamodder.vkfeed.api;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by ilya on 20.08.16.
 */
public class VkAuthenticator implements Authenticator {
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        String token = VKAccessToken.currentToken().accessToken;

        Request request = response.request();
        if (request.url().queryParameter("access_token").equals(token)) {
            VKSdk.logout();
            return null;
        }

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("access_token", token)
                .build();
        return response.request().newBuilder().url(url).build();
    }
}
