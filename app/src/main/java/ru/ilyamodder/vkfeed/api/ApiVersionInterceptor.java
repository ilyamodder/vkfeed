package ru.ilyamodder.vkfeed.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ilya on 20.08.16.
 */
class ApiVersionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("v", "5.53")
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
