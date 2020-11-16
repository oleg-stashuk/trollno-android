package com.apps.trollino.adapters;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class NewsVideoAdapter extends BaseRecyclerAdapter<FavoriteModel> {

    public NewsVideoAdapter(BaseActivity baseActivity, List<FavoriteModel> items, OnItemClick<FavoriteModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<FavoriteModel> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_news_video;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final FavoriteModel item) {
                RelativeLayout video = itemView.findViewById(R.id.video_new_image_item);
                TextView commentCountTextView = itemView.findViewById(R.id.comment_count_new_video_item);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_new_video_item);

                titleVideoTextView.setText(item.getVideoTitle());
                commentCountTextView.setText(String.valueOf(item.getCommentCount()));
            }
        };
    }
}
