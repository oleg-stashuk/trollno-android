package com.apps.trollino.adapters;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.data.Const.BASE_URL;
import static com.apps.trollino.utils.data.Const.CATEGORY_DISCUSSED;

public class DiscussPostsAdapter extends BaseRecyclerAdapter<PostsModel.PostDetails> {
    private final PrefUtils prefUtils;
    private final int widthImage;
    private final int heightImage;
    private final LinearLayoutManager linearLayoutManager;

    public DiscussPostsAdapter(BaseActivity baseActivity, PrefUtils prefUtils, LinearLayoutManager linearLayoutManager,
                               List<PostsModel.PostDetails> items, OnItemClick<PostsModel.PostDetails> onItemClick) {
        super(baseActivity, items, onItemClick);
        this.prefUtils = prefUtils;
        this.linearLayoutManager = linearLayoutManager;
        widthImage = prefUtils.getImageWidthForOneColumn();
        heightImage = widthImage / 3 * 2;
    }

    @Override
    public void setOnItemClick(OnItemClick<PostsModel.PostDetails> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_discuss_post;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final PostsModel.PostDetails item) {
                FrameLayout frameLayout = itemView.findViewById(R.id.frame_layout_discuss_post);
                LinearLayout linearLayout = itemView.findViewById(R.id.item_discuss_post);
                RelativeLayout imageRelativeLayout = itemView.findViewById(R.id.image_layout_item_discuss_post);
                ImageView postImageView = itemView.findViewById(R.id.image_item_discuss_post);
                TextView commentCountTextView = itemView.findViewById(R.id.comment_count_discuss_post);
                TextView titleTextView = itemView.findViewById(R.id.title_discuss_post);

                int firstCompletelyVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

                CategoryStoreProvider.getInstance(view.getContext())
                        .updatePositionInCategory(CATEGORY_DISCUSSED, firstCompletelyVisiblePosition);

                if(!prefUtils.getIsUserAuthorization() ||
                        (!item.isRead() && prefUtils.getIsUserAuthorization()) ||
                        !prefUtils.isShowReadPost()) {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.white));
                    titleTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.black));
                    frameLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorLightTransparent));
                    titleTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blackTransparent));
                    frameLayout.setVisibility(View.VISIBLE);
                }

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(widthImage, heightImage);
                imageRelativeLayout.getLayoutParams().height = heightImage;
                imageRelativeLayout.getLayoutParams().width = widthImage;
                postImageView.setLayoutParams(layoutParams);

                String imageUrl = BASE_URL.concat(item.getImageUrl());
                Picasso
                        .get()
                        .load(imageUrl)
                        .into(postImageView);

                titleTextView.setText(item.getTitle());
                commentCountTextView.setText(item.getCommentCount());
            }
        };
    }
}
