package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.apps.trollino.utils.Const.BASE_URL;

public class PostListAdapter extends BaseRecyclerAdapter<PostsModel.PostDetails> {

    public PostListAdapter(BaseActivity baseActivity, List<PostsModel.PostDetails> items, OnItemClick<PostsModel.PostDetails> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<PostsModel.PostDetails> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_post_list_for_two_columns;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final PostsModel.PostDetails item) {
                ImageView postImageView = itemView.findViewById(R.id.image_post_two_columns);
                ImageView imageDiscussImageView = itemView.findViewById(R.id.discuss_image_post_two_columns);
                TextView textDiscussImageView = itemView.findViewById(R.id.discuss_text_post_two_columns);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_post_two_columns);

                String imageUrl = BASE_URL.concat(item.getImageUrl());
                Glide
                        .with(view.getContext())
                        .load(imageUrl)
                        .centerCrop()
                        .placeholder(R.color.colorGreyBackgroundVideo)
                        .fallback(R.color.colorGreyBackgroundVideo)
                        .into(postImageView);

                titleVideoTextView.setText(item.getTitle());
                if(item.getCommentActiveDiscus() != 0) {
                    imageDiscussImageView.setVisibility(View.VISIBLE);
                    textDiscussImageView.setVisibility(View.VISIBLE);
                } else {
                    imageDiscussImageView.setVisibility(View.GONE);
                    textDiscussImageView.setVisibility(View.GONE);
                }
            }
        };
    }
}
