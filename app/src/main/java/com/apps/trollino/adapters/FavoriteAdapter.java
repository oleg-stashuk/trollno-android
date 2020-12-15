package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.Const.BASE_URL;

public class FavoriteAdapter extends BaseRecyclerAdapter<PostsModel.PostDetails> {

    public FavoriteAdapter(BaseActivity baseActivity, List<PostsModel.PostDetails> items, OnItemClick<PostsModel.PostDetails> onItemClick) {
        super(baseActivity, items, onItemClick);
    }

    @Override
    public void setOnItemClick(OnItemClick<PostsModel.PostDetails> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_favorite_posts;
    }

    @Override
    protected BaseItem createViewHolder(View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final PostsModel.PostDetails item) {
                ImageView imageView = itemView.findViewById(R.id.image_favorite_item);
                TextView titleTextView = itemView.findViewById(R.id.title_favorite_item);
                ImageButton deleteImageButton = itemView.findViewById(R.id.delete_button_favorite_item);

                titleTextView.setText(item.getTitle());
                Picasso
                        .get()
                        .load(BASE_URL.concat(item.getImageUrl()))
                        .into(imageView);

                deleteImageButton.setOnClickListener(view1 -> Toast.makeText(itemView.getContext(), "Delete", Toast.LENGTH_SHORT).show());
            }
        };
    }
}
