package com.example.pagingtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pagingtest.R;
import com.example.pagingtest.model.GithubRes;

/**
 * @author 胡森
 * @description
 * @date 2020-10-27
 */
public class GithubPagedAdapter extends PagedListAdapter<GithubRes.Item, RecyclerView.ViewHolder> {
    private final Context mContext;

    public GithubPagedAdapter(Context context) {
        super(DIFF_CALLBACK);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GithubRes.Item item = getItem(position);
        if (item == null)
            return;
        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        contentViewHolder.forkCount.setText(String.valueOf(item.forkCount));
        contentViewHolder.starCount.setText(String.valueOf(item.starCount));
        contentViewHolder.projectDescription.setText(String.valueOf(item.description));
        contentViewHolder.ownerName.setText(String.valueOf(item.owner.name));
        contentViewHolder.projectName.setText(String.valueOf(item.name));
        Glide.with(mContext).load(item.owner.avatar)
                .override(contentViewHolder.ownerAvatar.getWidth(), contentViewHolder.ownerAvatar.getHeight())
                .into(contentViewHolder.ownerAvatar);
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        public ImageView ownerAvatar;
        public TextView projectName;
        public TextView ownerName;
        public TextView projectDescription;
        public TextView starCount;
        public TextView forkCount;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            ownerAvatar = itemView.findViewById(R.id.owner_avatar);
            projectName = itemView.findViewById(R.id.project_name);
            ownerName = itemView.findViewById(R.id.owner_name);
            projectDescription = itemView.findViewById(R.id.project_description);
            starCount = itemView.findViewById(R.id.star_count);
            forkCount = itemView.findViewById(R.id.fork_count);
        }
    }

    private final static DiffUtil.ItemCallback<GithubRes.Item> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GithubRes.Item>() {
                @Override
                public boolean areItemsTheSame(@NonNull GithubRes.Item oldItem, @NonNull GithubRes.Item newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull GithubRes.Item oldItem, @NonNull GithubRes.Item newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
