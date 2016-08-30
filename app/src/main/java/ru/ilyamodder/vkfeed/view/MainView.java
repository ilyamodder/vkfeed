package ru.ilyamodder.vkfeed.view;

import java.util.List;

import ru.ilyamodder.vkfeed.model.local.JoinedPost;

/**
 * Created by ilya on 24.08.16.
 */

public interface MainView {
    void showFeed(List<JoinedPost> posts);

    void showLoading();

    void hideLoading();

    void showError();

    void showLoginActivity();

    void showPostActivity(long id, long srcId);
}
