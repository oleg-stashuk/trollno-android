package com.apps.trollino.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ShowTimeAgoHelper;
import com.apps.trollino.utils.data.PrefUtils;

import java.util.List;

public class UserCommentAdapter extends BaseRecyclerAdapter<CommentModel.Comments> {
    private PrefUtils prefUtils;

    public UserCommentAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<CommentModel.Comments> items, OnItemClick<CommentModel.Comments> onItemClick) {
        super(baseActivity, items, onItemClick);
        this.prefUtils = prefUtils;
    }

    @Override
    public void setOnItemClick(OnItemClick<CommentModel.Comments> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_user_comment;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(CommentModel.Comments item) {
                TextView titleTextView = view.findViewById(R.id.title_item_user_comment);
                TextView commentTextView = view.findViewById(R.id.comment_item_user_comment);
                TextView countLikeTextView = view.findViewById(R.id.count_like_item_user_comment);
                ImageView indicatorImageView = view.findViewById(R.id.new_comment_indicator_item_user_comment);
                TextView newCommentTextView = view.findViewById(R.id.new_comment_item_user_comment);
                TextView timeTextView = view.findViewById(R.id.time_item_user_comment);

                prefUtils.saveCurrentAdapterPositionAnswers(getAdapterPosition());
                titleTextView.setText(item.getPostTitle());
                commentTextView.setText(item.getCommentBody());
                countLikeTextView.setText(item.getCountLike());

                int countNewAnswer = Integer.parseInt(item.getCommentNewAnswersCount());
                if(countNewAnswer > 0) {
                    indicatorImageView.setVisibility(View.VISIBLE);
                    newCommentTextView.setVisibility(View.VISIBLE);
                } else {
                    indicatorImageView.setVisibility(View.GONE);
                    newCommentTextView.setVisibility(View.GONE);
                }

                timeTextView.setText(ShowTimeAgoHelper.showTimeAgo(item.getCreated()));
            }
        };
    }
}
