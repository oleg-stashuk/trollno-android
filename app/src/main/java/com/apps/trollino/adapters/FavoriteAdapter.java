package com.apps.trollino.adapters;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.single_post.PostUnbookmark;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.Const.BASE_URL;

public class FavoriteAdapter extends BaseRecyclerAdapter<PostsModel.PostDetails> {
    private PrefUtils prefUtils;

    public FavoriteAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<PostsModel.PostDetails> items, OnItemClick<PostsModel.PostDetails> onItemClick) {
        super(baseActivity, items, onItemClick);
        this.prefUtils = prefUtils;
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

                deleteImageButton.setOnClickListener(view1 -> {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                    dialogBuilder.setMessage(view.getContext().getResources().getString(R.string.do_you_really_want_to_delete_post_from_favorite))
                            .setPositiveButton(R.string.remove_account_confirm_button, (dialog, which) -> {
                                new Thread(() -> PostUnbookmark.removePostFromFavorite(view.getContext(), prefUtils, item.getPostId(), null)).start();
                                dialog.cancel();
                            })
                            .setNegativeButton(R.string.no_button_for_dialog, (dialog, which) -> {
                                dialog.cancel();
                            });

                    AlertDialog dialog = dialogBuilder.create();
                    dialog.show();
                });
            }
        };
    }
}
