package com.apps.trollino.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ClickableSpanText;
import com.apps.trollino.utils.ShowTimeAgoHelper;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking.comment.GetCommentListByComment;
import com.apps.trollino.utils.networking.comment.PostLikeToComment;
import com.apps.trollino.utils.networking.comment.PostUnlikeToComment;
import com.apps.trollino.utils.networking.user_action.PostMarkReadAllAnswersToComment;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.Const.BASE_URL;

public class CommentToPostParentAdapter extends BaseRecyclerAdapter<CommentModel.Comments> {
    private EditText commentEditText;
    private PrefUtils prefUtils;

    public CommentToPostParentAdapter(BaseActivity baseActivity, PrefUtils prefUtils, List<CommentModel.Comments> items, EditText commentEditText) {
        super(baseActivity, items);
        this.commentEditText = commentEditText;
        this.prefUtils = prefUtils;
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_single_comment_parent;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final CommentModel.Comments item) {
                ImageView imageImageView = view.findViewById(R.id.image_user_single_comment_parent);
                TextView nameTextView = view.findViewById(R.id.name_user_single_comment_parent);
                TextView timeTextView = view.findViewById(R.id.time_user_single_comment_parent);
                final TextView commentBodyTextView = view.findViewById(R.id.comment_user_single_comment_parent);
                final ImageView likeImageView = view.findViewById(R.id.like_single_comment_parent);
                TextView countLikeTextView = view.findViewById(R.id.count_like_single_comment_parent);
                TextView answerTextView = view.findViewById(R.id.answer_single_comment_parent); // button
                TextView showMoreTextView = view.findViewById(R.id.show_more_comment_single_comment_parent); // button
                RecyclerView childCommentRecyclerView = view.findViewById(R.id.recycler_item_single_comment_parent);

                if(item.getCommentId().equals(prefUtils.getCommentIdForActivity())) {
                    commentBodyTextView.setTextColor(Color.parseColor("#DD6AA0"));
                    new Thread(() -> PostMarkReadAllAnswersToComment.PostMarkReadAllAnswersToComment(view.getContext(), prefUtils, item.getCommentId())).start();
                }

                Picasso
                        .get()
                        .load(BASE_URL.concat(item.getUrlUserImage()))
                        .into(imageImageView);

                nameTextView.setText(item.getAuthorName());
                timeTextView.setText(ShowTimeAgoHelper.showTimeAgo(item.getTime()));

                final String comment = item.getCommentBody();
                checkCommentLength(commentBodyTextView, comment, view.getContext()); // Проверить длинну комментария + обработка нажатия на кнопку "Весь комментарий"

                boolean isLike = item.getFavoriteFlag().equals("1") ? true : false;
                changeLikeImage(isLike, likeImageView); // Проверить пользователь оценил комент или нет

                countLikeTextView.setText(item.getCountLike());
                likeImageClickListener(view.getContext(), likeImageView, item.getCommentId(), isLike); // обработка нажатия на кномку "оценить комент"

                answerClickListener(answerTextView, item.getAuthorName(), item.getCommentId()); // обработка нажатия на кнопку "Ответить"

                // Загрузить дочерние комментарии
                GetCommentListByComment.getCommentListByComment(view.getContext(), prefUtils, item.getCommentId(),
                        childCommentRecyclerView, showMoreTextView, commentEditText, view);
            }


            private void changeLikeImage(boolean isLike, ImageView imageView) {
                if(isLike) {
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

            private void likeImageClickListener(Context context, final ImageView imageView, String commentId, Boolean isLike) {
                imageView.setOnClickListener(v -> {
                    if (prefUtils.getIsUserAuthorization()) {
                        if (isLike) {
                            new Thread(() -> PostUnlikeToComment.postUnlikeToComment(context, prefUtils, commentId, view)).start();
                        } else {
                            new Thread(() -> PostLikeToComment.postLikeToComment(context, prefUtils, commentId, view)).start();
                        }
                    } else {
                        GuestDialog dialog = new GuestDialog();
                        dialog.showDialog(context);
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
