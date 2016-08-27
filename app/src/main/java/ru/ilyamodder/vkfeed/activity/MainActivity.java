package ru.ilyamodder.vkfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.vk.sdk.VKSdk;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.adapter.MainAdapter;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.presenter.MainPresenter;
import ru.ilyamodder.vkfeed.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainPresenter mPresenter;
    private MainAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mNeedToLoadMore = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainAdapter(this, id -> mPresenter.onItemClick(id));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int pastVisiblesItems, visibleItemCount, totalItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems + MainPresenter.ROWS_PER_PAGE / 2)
                        >= totalItemCount && !mSwipeRefreshLayout.isRefreshing()
                        && mNeedToLoadMore) {
                    mPresenter.loadMore();
                    mNeedToLoadMore = false;
                }
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.refresh());

        mPresenter = new MainPresenter(this, this);
        mPresenter.onActivityCreate(savedInstanceState);
        if (!VKSdk.isLoggedIn()) {
            LoginActivity.startAsRootActivity(this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void showFeed(List<JoinedPost> posts) {
        mAdapter.setPosts(posts);
        mNeedToLoadMore = true;
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginActivity() {
        LoginActivity.startAsRootActivity(this);
    }
}
