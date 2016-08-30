package ru.ilyamodder.vkfeed.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ilyamodder.vkfeed.R;

/**
 * Created by ilya on 30.08.16.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {
    private List<String> mUrls;

    public PhotosAdapter(List<String> urls) {
        mUrls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.mPhoto.getContext())
                .load(mUrls.get(position))
                .into(holder.mPhoto);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView mPhoto;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
