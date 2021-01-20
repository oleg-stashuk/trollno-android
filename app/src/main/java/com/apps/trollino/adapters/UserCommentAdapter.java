package com.apps.trollino.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ShowTimeAgoHelper;

import java.util.List;

public class UserCommentAdapter extends BaseRecyclerAdapter<CommentModel.Comments> {

    public UserCommentAdapter(BaseActivity baseActivity, List<CommentModel.Comments> items, OnItemClick<CommentModel.Comments> onItemClick) {
        super(baseActivity, items, onItemClick);
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
                LinearLayout linearLayout = view.findViewById(R.id.background_liner_layout_user_comment);
                TextView titleTextView = view.findViewById(R.id.title_item_user_comment);
//                ImageView menuImageView = view.findViewById(R.id.menu_item_user_comment);
                TextView commentTextView = view.findViewById(R.id.comment_item_user_comment);
                TextView countLikeTextView = view.findViewById(R.id.count_like_item_user_comment);
                ImageView indicatorImageView = view.findViewById(R.id.new_comment_indicator_item_user_comment);
                TextView newCommentTextView = view.findViewById(R.id.new_comment_item_user_comment);
                TextView timeTextView = view.findViewById(R.id.time_item_user_comment);

                titleTextView.setText(item.getPostTitle());
                commentTextView.setText(item.getCommentBody());
                countLikeTextView.setText(item.getCountLike());

                int countNewAnswer = Integer.parseInt(item.getCommentNewAnswersCount());
                if(countNewAnswer > 0) {
                    indicatorImageView.setVisibility(View.VISIBLE);
                    newCommentTextView.setVisibility(View.VISIBLE);
                    newCommentTextView.setText(view.getContext().getString(R.string.txt_new_answers) + countNewAnswer);
                } else {
                    indicatorImageView.setVisibility(View.GONE);
                    newCommentTextView.setVisibility(View.GONE);
                }

                timeTextView.setText(ShowTimeAgoHelper.showTimeAgo(item.getCreated()));

//                menuImageView.setOnClickListener(v ->
//                        Toast.makeText(view.getContext(), "Кнопка меню", Toast.LENGTH_SHORT).show()
//                );
            }
        };
    }
}
