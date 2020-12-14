package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class FavoriteAdapter extends BaseRecyclerAdapter<FavoriteModel> {

    public FavoriteAdapter(BaseActivity baseActivity, List<FavoriteModel> items, OnItemClick<FavoriteModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<FavoriteModel> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_favorite_video;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final FavoriteModel item) {
                LinearLayout video = itemView.findViewById(R.id.video_favorite_video_item);
                TextView titleVideoTextView = itemView.findViewById(R.id.title_favorite_video_item);
                ImageButton deleteImageButton = itemView.findViewById(R.id.delete_button_favorite_video_item);

                titleVideoTextView.setText(item.getVideoTitle());

                deleteImageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
    }
}
