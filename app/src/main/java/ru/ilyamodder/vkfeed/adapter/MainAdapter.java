package ru.ilyamodder.vkfeed.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import ru.ilyamodder.vkfeed.R;
import ru.ilyamodder.vkfeed.model.local.JoinedPost;
import rx.functions.Action1;

/**
 * Created by ilya on 25.08.16.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<JoinedPost> mPosts;
    private Context mContext;
    private Action1<Long> mClickListener;

    public MainAdapter(Context context, Action1<Long> clickListener) {
        mPosts = new ArrayList<>();
        mContext = context;
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JoinedPost post = mPosts.get(position);
        holder.mCaption.setText(post.getName());
        holder.mText.setText(post.getText());
        Glide.with(mContext)
                .load(post.getAvatar())
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.mAvatar);
        holder.mDate.setText(DateUtils.getRelativeTimeSpanString(mContext,
                post.getDate().getTime() * 1000));

        holder.itemView.setOnClickListener(view -> mClickListener.call(post.getId()));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setPosts(List<JoinedPost> posts) {
        mPosts = posts;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        ImageView mAvatar;
        @BindView(R.id.caption)
        TextView mCaption;
        @BindView(R.id.date)
        TextView mDate;
        @BindView(R.id.text)
        TextView mText;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
