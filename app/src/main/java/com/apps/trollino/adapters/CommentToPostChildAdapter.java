package com.apps.trollino.adapters;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ClickableSpanText;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.Const.BASE_URL;

public class CommentToPostChildAdapter extends BaseRecyclerAdapter<CommentModel.Comments> {
    private EditText commentEditText;
    private boolean isUserLikeIt;
    private PrefUtils prefUtils;

    public CommentToPostChildAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<CommentModel.Comments> items, EditText commentEditText) {
        super(baseActivity, items);
        this.commentEditText = commentEditText;
        this.prefUtils = prefUtils;
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_single_comment_child;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final CommentModel.Comments item) {
                ImageView imageImageView = view.findViewById(R.id.image_user_single_comment_child);
                TextView nameTextView = view.findViewById(R.id.name_user_single_comment_child);
                TextView timeTextView = view.findViewById(R.id.time_user_single_comment_child);
                final TextView commentTextView = view.findViewById(R.id.comment_user_single_comment_child);
                final ImageView likeImageView = view.findViewById(R.id.like_single_comment_child);
                TextView countLikeTextView = view.findViewById(R.id.count_like_single_comment_child);
                TextView answerTextView = view.findViewById(R.id.answer_single_comment_child); // button

                final String comment = item.getCommentBody();
                isUserLikeIt = item.getFavoriteFlag().equals("1") ? true : false;

                Picasso
                        .get()
                        .load(BASE_URL.concat(item.getUrlUserImage()))
                        .into(imageImageView);

                nameTextView.setText(item.getAuthorName());
                timeTextView.setText(item.getTime());
                countLikeTextView.setText(item.getCountLike());

                checkCommentLength(commentTextView, comment, view.getContext()); // Проверить длинну комментария
                changeLikeImage(item.getFavoriteFlag(), likeImageView); // Проверить пользователь оценил комент или нет
                likeImageClickListener(likeImageView); // обработка нажатия на кнопку "оценить комент"
                answerClickListener(answerTextView, item.getAuthorName(), item.getCommentId()); // обработка нажатия на кнопку "Ответить"
            }

            private void changeLikeImage(String isLike, ImageView imageView) {
                if(isLike.equals("1")) {
                    imageView.setImageResource(R.drawable.ic_favorite_color);
                } else {
                    imageView.setImageResource(R.drawable.ic_favorite_border_color);
                }
            }

            private void answerClickListener(TextView textView, final String name, String commentId) {
                textView.setOnClickListener(v -> {
                    commentEditText.requestFocus();
                    commentEditText.setText(name.concat(", "));
                    commentEditText.setSelection(commentEditText.getText().length());

                    prefUtils.saveCommentIdToAnswer(commentId);
                    prefUtils.saveAnswerToUserName(name);
                });
            }

            private void likeImageClickListener(final ImageView imageView) {
                imageView.setOnClickListener(v -> {
                    isUserLikeIt = !isUserLikeIt;
                    String favoriteFlag = isUserLikeIt ? "1" : "0";
                    changeLikeImage(favoriteFlag, imageView);
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
