package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.SettingsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AvatarsAdapter extends BaseRecyclerAdapter<SettingsModel.AvatarImageModel> {

    public AvatarsAdapter(BaseActivity baseActivity, List<SettingsModel.AvatarImageModel> items, OnItemClick<SettingsModel.AvatarImageModel> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<SettingsModel.AvatarImageModel> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_image_avatar;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(SettingsModel.AvatarImageModel item) {
                ImageView imageView = view.findViewById(R.id.image_avatar_item);

                Picasso
                        .get()
                        .load(item.getAvatarUrl())
                        .into(imageView);
            }
        };
    }
}
