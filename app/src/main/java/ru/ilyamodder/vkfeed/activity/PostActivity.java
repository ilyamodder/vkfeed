package ru.ilyamodder.vkfeed.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.ilyamodder.vkfeed.R;

/**
 * Created by ilya on 30.08.16.
 */

public class PostActivity extends AppCompatActivity {
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
    }

    @OnClick(R.id.like)
    public void onLikeClick() {

    }
}
