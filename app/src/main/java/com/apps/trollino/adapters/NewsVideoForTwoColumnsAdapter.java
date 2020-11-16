package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class NewsVideoForTwoColumnsAdapter extends BaseRecyclerAdapter<FavoriteModel> {

    public NewsVideoForTwoColumnsAdapter(BaseActivity baseActivity, List<FavoriteModel> items, OnItemClick<FavoriteModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<FavoriteModel> onItemClick) {
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
            public void bind(final FavoriteModel item) {
                RelativeLayout video = itemView.findViewById(R.id.image_post_two_columns);
                ImageView imageDiscussImageView = itemView.findViewById(R.id.discuss_image_post_two_columns);
                TextView textDiscussImageView = itemView.findViewById(R.id.discuss_text_post_two_columns);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_post_two_columns);

                titleVideoTextView.setText(item.getVideoTitle());
                if(item.isInDiscuss()) {
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
