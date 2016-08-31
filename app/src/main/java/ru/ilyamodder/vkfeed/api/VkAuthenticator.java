package ru.ilyamodder.vkfeed.api;

import com.vk.sdk.VKSdk;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by ilya on 20.08.16.
 */
public class VkAuthenticator implements Authenticator {
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        VKSdk.logout();
        return null;
    }
}
