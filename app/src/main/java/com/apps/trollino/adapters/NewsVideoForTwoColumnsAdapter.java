package com.apps.trollino.adapters;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.FavoriteVideoModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class NewsVideoForTwoColumnsAdapter extends BaseRecyclerAdapter<FavoriteVideoModel> {

    public NewsVideoForTwoColumnsAdapter(BaseActivity baseActivity, List<FavoriteVideoModel> items, OnItemClick<FavoriteVideoModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<FavoriteVideoModel> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_news_video_for_two_collumns;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final FavoriteVideoModel item) {
                RelativeLayout video = itemView.findViewById(R.id.video_new_video_item_for_two_columns);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_new_video_item_for_two_columns);

                titleVideoTextView.setText(item.getVideoTitle());

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(itemView.getContext(), "Video " + item.getVideoId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }
}
