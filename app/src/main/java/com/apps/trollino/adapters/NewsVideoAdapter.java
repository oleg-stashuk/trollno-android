package com.apps.trollino.adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.FavoriteVideoModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class NewsVideoAdapter extends BaseRecyclerAdapter<FavoriteVideoModel> {

    public NewsVideoAdapter(BaseActivity baseActivity, List<FavoriteVideoModel> items, OnItemClick<FavoriteVideoModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<FavoriteVideoModel> onItemClick) {
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
            public void bind(final FavoriteVideoModel item) {
                RelativeLayout video = itemView.findViewById(R.id.video_new_video_item);
                TextView commentCountTextView = itemView.findViewById(R.id.comment_count_new_video_item);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_new_video_item);

                titleVideoTextView.setText(item.getVideoTitle());
                commentCountTextView.setText(String.valueOf(item.getCommentCount()));

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
