package com.apps.trollino.adapters;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.data.Const.BASE_URL;

public class PostListAdapter extends BaseRecyclerAdapter<PostsModel.PostDetails> {
    private PrefUtils prefUtils;
    private int widthImage;
    private  int heightImage;

    public PostListAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<PostsModel.PostDetails> items, OnItemClick<PostsModel.PostDetails> onItemClick) {
        super(baseActivity, items, onItemClick);
        this.prefUtils = prefUtils;
        widthImage = prefUtils.getImageWidth();
        heightImage = widthImage / 3 * 2;
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
                FrameLayout frameLayout = itemView.findViewById(R.id.frame_layout_post_two_columns);
                LinearLayout linearLayout = itemView.findViewById(R.id.item_post_list_for_two_columns);
                ImageView postImageView = itemView.findViewById(R.id.image_post_two_columns);
                ImageView imageDiscussImageView = itemView.findViewById(R.id.discuss_image_post_two_columns);
                TextView textDiscussImageView = itemView.findViewById(R.id.discuss_text_post_two_columns);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_post_two_columns);

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(widthImage, heightImage);
                postImageView.setLayoutParams(layoutParams);

                prefUtils.saveCurrentAdapterPositionPosts(getAdapterPosition());
                if(!prefUtils.getIsUserAuthorization() || (item.getRead() == 0 && prefUtils.getIsUserAuthorization()) || !prefUtils.isShowReadPost()) {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
                    titleVideoTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
                    frameLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorLightTransparent));
                    titleVideoTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blackTransparent));
                    frameLayout.setVisibility(View.VISIBLE);
                }

                String imageUrl = BASE_URL.concat(item.getImageUrl());
                Picasso
                        .get()
                        .load(imageUrl)
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
