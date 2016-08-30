package ru.ilyamodder.vkfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import ru.ilyamodder.vkfeed.presenter.PostPresenter;
import ru.ilyamodder.vkfeed.view.PostView;

/**
 * Created by ilya on 30.08.16.
 */

public class PostActivity extends AppCompatActivity implements PostView {
    public static final String EXTRA_POST_ID = "post_id";
    public static final String EXTRA_SOURCE_ID = "source_id";
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.caption)
    TextView mCaption;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.likesCount)
    TextView mLikesCount;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private PostPresenter mPresenter;

    public static void start(Context context, long postId, long sourceId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        intent.putExtra(EXTRA_SOURCE_ID, sourceId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mPresenter = new PostPresenter(this, this, getIntent().getLongExtra(EXTRA_POST_ID, 0),
                getIntent().getLongExtra(EXTRA_SOURCE_ID, 0));
        mPresenter.onActivityCreate();
    }

    @Override
    public void showPost(JoinedPost post) {
        mCaption.setText(post.getName());
        mDate.setText(DateUtils.getRelativeTimeSpanString(this, post.getDate().getTime()));
        mText.setText(post.getText());
        mLikesCount.setText(String.valueOf(post.getLikesCount()));
        Glide.with(this)
                .load(post.getAvatar())
                .into(mAvatar);
    }
}
