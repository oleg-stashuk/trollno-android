package com.apps.trollino.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.data.Const.BASE_URL;
import static com.apps.trollino.utils.data.Const.CATEGORY_FRESH;

public class FreshPostAdapter extends RecyclerView.Adapter<FreshPostAdapter.FreshPostHolder>{
    private List<PostsModel.PostDetails> postList;

    private final PrefUtils prefUtils;
    private final int widthImage;
    private final int heightImage;
    private final boolean isPostListFromCategory;
    private final GridLayoutManager gridLayoutManager;
    private OnItemClick<PostsModel.PostDetails> onItemClick;

    public FreshPostAdapter(PrefUtils prefUtils, GridLayoutManager gridLayoutManager,
                            List<PostsModel.PostDetails> postList, OnItemClick<PostsModel.PostDetails> onItemClick) {
        this.postList = postList;
        this.prefUtils = prefUtils;
        this.gridLayoutManager = gridLayoutManager;
        widthImage = prefUtils.getImageWidth();
        heightImage = widthImage / 3 * 2;
        isPostListFromCategory = prefUtils.isPostFromCategoryList();
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FreshPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_post_list_for_two_columns, parent, false);
        return new FreshPostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FreshPostHolder holder, int position) {
        holder.bindTo(postList.get(position), onItemClick);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addItems(List<PostsModel.PostDetails> items) {
        this.postList.clear();
        this.postList.addAll(items);
        notifyDataSetChanged();
    }

    public class FreshPostHolder extends RecyclerView.ViewHolder {
        private FrameLayout frameLayout ;
        private LinearLayout linearLayout;
        private ImageView postImageView;
        private ImageView imageDiscussImageView;
        private TextView textDiscussImageView;
        private TextView titleTextView;

        public FreshPostHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame_layout_post_two_columns);
            linearLayout = itemView.findViewById(R.id.item_post_list_for_two_columns);
            postImageView = itemView.findViewById(R.id.image_post_two_columns);
            imageDiscussImageView = itemView.findViewById(R.id.discuss_image_post_two_columns);
            textDiscussImageView = itemView.findViewById(R.id.discuss_text_post_two_columns);
            titleTextView = itemView.findViewById(R.id.title_post_two_columns);
        }

        public void bindTo(PostsModel.PostDetails item, OnItemClick<PostsModel.PostDetails> itemClickListener) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(widthImage, heightImage);
            postImageView.setLayoutParams(layoutParams);

            CategoryModel category = CategoryStoreProvider.getInstance(itemView.getContext()).getCategoryById(CATEGORY_FRESH);

            // Сохранить просмотренную позицию в БД
            int position = getAdapterPosition();
            if (position % 2 == 0) {
                int positionFirstCompletelyVisible = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(position == 0) {
                    positionFirstCompletelyVisible=  0;
                } else if(position != 0 && positionFirstCompletelyVisible < 0) {
                    positionFirstCompletelyVisible = category.getPostInCategory() + 2;
                }
                category.setPostInCategory(positionFirstCompletelyVisible);
                CategoryStoreProvider.getInstance(itemView.getContext()).updateCategory(category);
            }

            if(!prefUtils.getIsUserAuthorization() ||
                    (!item.isRead() && prefUtils.getIsUserAuthorization()) ||
                    !prefUtils.isShowReadPost()) {
                linearLayout.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
                titleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.black));
                frameLayout.setVisibility(View.GONE);
            } else {
                linearLayout.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorLightTransparent));
                titleTextView.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blackTransparent));
                frameLayout.setVisibility(View.VISIBLE);
            }

            String imageUrl = BASE_URL.concat(item.getImageUrl());
            Picasso
                    .get()
                    .load(imageUrl)
                    .into(postImageView);

            titleTextView.setText(item.getTitle());
            if(item.isCommentActiveDiscus()) {
                imageDiscussImageView.setVisibility(View.VISIBLE);
                textDiscussImageView.setVisibility(View.VISIBLE);
            } else {
                imageDiscussImageView.setVisibility(View.GONE);
                textDiscussImageView.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(view -> itemClickListener.onItemClick(item));
        }
    }

    public interface OnItemClick<PostDetails> {
        void onItemClick(PostDetails item);
    }
}
