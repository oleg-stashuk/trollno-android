package com.apps.trollino.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ClickableSpanText;

import java.util.List;

class CommentToPostChildAdapter extends BaseRecyclerAdapter<UserCommentActivityModel> {
    EditText commentEditText;
    boolean isUserLikeIt;

    public CommentToPostChildAdapter(BaseActivity baseActivity, List<UserCommentActivityModel> items, EditText commentEditText) {
        super(baseActivity, items);
        this.commentEditText = commentEditText;
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_single_comment_child;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final UserCommentActivityModel item) {
                ImageView imageImageView = view.findViewById(R.id.image_user_single_comment_child);
                TextView nameTextView = view.findViewById(R.id.name_user_single_comment_child);
                TextView timeTextView = view.findViewById(R.id.time_user_single_comment_child);
                final TextView commentTextView = view.findViewById(R.id.comment_user_single_comment_child);
                final ImageView likeImageView = view.findViewById(R.id.like_single_comment_child);
                TextView countLikeTextView = view.findViewById(R.id.count_like_single_comment_child);
                TextView answerTextView = view.findViewById(R.id.answer_single_comment_child); // button

                final String comment = item.getComment();
                isUserLikeIt = item.isUserLikeIt();

                imageImageView.setImageResource(item.getUserImage());
                nameTextView.setText(item.getUserName());
                timeTextView.setText(item.getTime());
                countLikeTextView.setText(item.getLikeCount());

                checkCommentLength(commentTextView, comment, view.getContext()); // Проверить длинну комментария
                changeLikeImage(isUserLikeIt, likeImageView); // Проверить пользователь оценил комент или нет
                likeImageClickListener(likeImageView); // обработка нажатия на кномку "оценить комент"
                answerClickListener(answerTextView, item.getUserName()); // обработка нажатия на кнопку "Ответить"
            }

            private void changeLikeImage(boolean isLike, ImageView imageView) {
                if(isLike) {
                    imageView.setImageResource(R.drawable.ic_favorite_color);
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_color);
                }
            }

            private void answerClickListener(TextView textView, final String name) {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentEditText.requestFocus();
                        commentEditText.setText(name.concat(", "));
                        commentEditText.setSelection(commentEditText.getText().length());
                    }
                });
            }

            private void likeImageClickListener(final ImageView imageView) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUserLikeIt = !isUserLikeIt;
                        changeLikeImage(isUserLikeIt, imageView);
                    }
                });
            }

            private void checkCommentLength(final TextView commentTextView, final String comment, Context context) {
                if (comment.length() > 100) {
                    ClickableSpanText.makeClickableSpanText(commentTextView, comment, context); // Добавить кликабельную часть текста + обработка нажатия
                } else {
                    commentTextView.setText(comment);
                }
            }

        };
    }
}