package com.app.trollno.adapters;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.db_room.category.CategoryStoreProvider;
import com.app.trollno.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.app.trollno.utils.data.Const.BASE_URL;
import static com.app.trollno.utils.data.Const.CATEGORY_DISCUSSED;

public class DiscussPostAdapter extends RecyclerView.Adapter<DiscussPostAdapter.FreshPostHolder>{
    private final List<PostsModel.PostDetails> postList;

    private final PrefUtils prefUtils;
    private final int widthImage;
    private final int heightImage;
    private final LinearLayoutManager linearLayoutManager;
    private final OnItemClick<PostsModel.PostDetails> onItemClick;

    public DiscussPostAdapter(PrefUtils prefUtils, LinearLayoutManager linearLayoutManager,
                              List<PostsModel.PostDetails> postList, OnItemClick<PostsModel.PostDetails> onItemClick) {
        this.postList = postList;
        this.prefUtils = prefUtils;
        this.linearLayoutManager = linearLayoutManager;
        widthImage = prefUtils.getImageWidthForOneColumn();
        heightImage = widthImage / 3 * 2;
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public FreshPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_discuss_post, parent, false);
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
        private final FrameLayout frameLayout ;
        private final LinearLayout linearLayout;
        private final RelativeLayout imageRelativeLayout;
        private final ImageView postImageView;
        private final TextView commentCountTextView;
        private final TextView titleTextView;

        public FreshPostHolder(@NonNull View itemView) {
            super(itemView);
            frameLayout = itemView.findViewById(R.id.frame_layout_discuss_post);
            linearLayout = itemView.findViewById(R.id.item_discuss_post);
            imageRelativeLayout = itemView.findViewById(R.id.image_layout_item_discuss_post);
            postImageView = itemView.findViewById(R.id.image_item_discuss_post);
            commentCountTextView = itemView.findViewById(R.id.comment_count_discuss_post);
            titleTextView = itemView.findViewById(R.id.title_discuss_post);
        }

        public void bindTo(PostsModel.PostDetails item, OnItemClick<PostsModel.PostDetails> itemClickListener) {
            int firstCompletelyVisiblePosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

            CategoryStoreProvider.getInstance(itemView.getContext())
                    .updatePositionInCategory(CATEGORY_DISCUSSED, firstCompletelyVisiblePosition);

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

            itemView.setOnClickListener(view -> itemClickListener.onItemClick(item));
        }
    }

    public interface OnItemClick<PostDetails> {
        void onItemClick(PostDetails item);
    }
}
