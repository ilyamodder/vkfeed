package ru.ilyamodder.vkfeed.repository;

import android.app.Activity;
import android.content.Intent;

import rx.Observer;

/**
 * Created by ilya on 20.08.16.
 */

public interface RemoteRepository {
    void login(Activity activity, String[] scope);
    boolean processActivityResult(int requestCode, int resultCode, Intent data,
                                  Observer<String> observer);
}
