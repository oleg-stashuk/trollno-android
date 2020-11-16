package com.apps.trollino.adapters;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ClickableSpanText;

import java.util.ArrayList;
import java.util.List;

public class CommentToPostParentAdapter extends BaseRecyclerAdapter<UserCommentActivityModel> {
    EditText commentEditText;
    boolean isUserLikeIt;

    public CommentToPostParentAdapter(BaseActivity baseActivity, List<UserCommentActivityModel> items, EditText commentEditText) {
        super(baseActivity, items);
        this.commentEditText = commentEditText;
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_single_comment_parent;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(final UserCommentActivityModel item) {
                ImageView imageImageView = view.findViewById(R.id.image_user_single_comment_parent);
                TextView nameTextView = view.findViewById(R.id.name_user_single_comment_parent);
                TextView timeTextView = view.findViewById(R.id.time_user_single_comment_parent);
                final TextView commentTextView = view.findViewById(R.id.comment_user_single_comment_parent);
                final ImageView likeImageView = view.findViewById(R.id.like_single_comment_parent);
                TextView countLikeTextView = view.findViewById(R.id.count_like_single_comment_parent);
                TextView answerTextView = view.findViewById(R.id.answer_single_comment_parent); // button
                TextView showMoreTextView = view.findViewById(R.id.show_more_comment_single_comment_parent); // button
                RecyclerView childCommentRecyclerView = view.findViewById(R.id.recycler_item_single_comment_parent);

                final String comment = item.getComment();
                isUserLikeIt = item.isUserLikeIt();

                imageImageView.setImageResource(item.getUserImage());
                nameTextView.setText(item.getUserName());
                timeTextView.setText(item.getTime());
                commentTextView.setText(comment);
                countLikeTextView.setText(item.getLikeCount());

                checkCommentLength(commentTextView, comment, view.getContext()); // Проверить длинну комментария + обработка нажатия на кнопку "Весь комментарий"
                changeLikeImage(isUserLikeIt, likeImageView); // Проверить пользователь оценил комент или нет
                likeImageClickListener(likeImageView); // обработка нажатия на кномку "оценить комент"
                answerClickListener(answerTextView, item.getUserName()); // обработка нажатия на кнопку "Ответить"
                checkAnswerToThisComment(item.isCommentHasAnswer(), childCommentRecyclerView, showMoreTextView); // Проверить наличие ответов к этому посту
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

            private void checkAnswerToThisComment(boolean isCommentHasAnswer, final RecyclerView childCommentRecyclerView, final TextView showMoreTextView) {
                if(isCommentHasAnswer) {
                    childCommentRecyclerView.setVisibility(View.VISIBLE);
                    final List<UserCommentActivityModel> commentsListToPost = UserCommentActivityModel.makeCommentsListToPostChild(); // получить весь список дочерних комментариев

                    // Если в дочернем списке ответов к коментарию больше 3 элементов, отображать кнопку "Показать все ответы"
                    if(commentsListToPost.size() > 3) {
                        showMoreTextView.setVisibility(View.VISIBLE);

                        final List<UserCommentActivityModel> commentsListSize2 = new ArrayList<>();
                        commentsListSize2.add(commentsListToPost.get(0));
                        commentsListSize2.add(commentsListToPost.get(1));
                        makeChildCommentRecyclerView(childCommentRecyclerView, commentsListSize2);

                        showMoreTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeChildCommentRecyclerView(childCommentRecyclerView, commentsListToPost);
                                showMoreTextView.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        makeChildCommentRecyclerView(childCommentRecyclerView, commentsListToPost);
                        showMoreTextView.setVisibility(View.GONE);
                    }

                } else {
                    childCommentRecyclerView.setVisibility(View.GONE);
                    showMoreTextView.setVisibility(View.GONE);
                }
            }

            // Создание childCommentRecyclerView
            private void makeChildCommentRecyclerView(RecyclerView childCommentRecyclerView, List<UserCommentActivityModel> commentList) {
                childCommentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                childCommentRecyclerView.setAdapter( new CommentToPostChildAdapter((BaseActivity) view.getContext(), commentList, commentEditText));
            }
        };
    }
}
