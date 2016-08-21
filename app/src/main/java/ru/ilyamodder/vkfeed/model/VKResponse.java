package ru.ilyamodder.vkfeed.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ilya on 21.08.16.
 */

public class VKResponse<R> {
    @SerializedName("response")
    private R mResponse;

    public VKResponse(R mResponse) {
        this.mResponse = mResponse;
    }

    public R getResponse() {
        return mResponse;
    }
}
