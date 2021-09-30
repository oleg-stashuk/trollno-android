package com.app.trollno.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.trollno.R;
import com.app.trollno.adapters.base.BaseRecyclerAdapter;
import com.app.trollno.data.model.user_action.AnswersModel;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.ShowTimeAgoHelper;
import com.app.trollno.utils.data.PrefUtils;

import java.util.List;

public class AnswersAdapter extends BaseRecyclerAdapter<AnswersModel.Answers> {
    private PrefUtils prefUtils;

    public AnswersAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<AnswersModel.Answers> items, OnItemClick<AnswersModel.Answers> onItemClick) {
        super(baseActivity, items, onItemClick);
        this.prefUtils = prefUtils;
    }

    @Override
    public void setOnItemClick(OnItemClick<AnswersModel.Answers> onItemClick) {
        super.setOnItemClick(onItemClick);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_answers;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(AnswersModel.Answers item) {
                TextView titleTextView = view.findViewById(R.id.title_item_for_answers);
                TextView commentTextView = view.findViewById(R.id.comment_item_for_answers);
                TextView countLikeTextView = view.findViewById(R.id.count_like_item_for_answers);
                ImageView indicatorImageView = view.findViewById(R.id.new_comment_indicator_item_for_answers);
                TextView newCommentTextView = view.findViewById(R.id.new_comment_item_for_answers);
                TextView timeTextView = view.findViewById(R.id.time_item_for_answers);

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

                timeTextView.setText(ShowTimeAgoHelper.showTimeAgo(item.getTime()));
            }
        };
    }
}
