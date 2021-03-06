package com.app.trollno.adapters;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.base.BaseRecyclerAdapter;
import com.app.trollno.data.model.comment.CommentModel;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.SpanText;
import com.app.trollno.utils.ShowTimeAgoHelper;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking.comment.GetCommentListByComment;
import com.app.trollno.utils.networking.comment.PostLikeToComment;
import com.app.trollno.utils.networking.comment.PostUnlikeToComment;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.app.trollno.utils.data.Const.BASE_URL;
import static com.app.trollno.utils.data.Const.COUNT_SYMBOL_TO_HIDE_PAR_OF_COMMENT;

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
                TextView hideCommentTextView = view.findViewById(R.id.hide_comment_single_comment_parent); // button
                RecyclerView childCommentRecyclerView = view.findViewById(R.id.recycler_item_single_comment_parent);

                prefUtils.saveCurrentAdapterPositionComment(getAdapterPosition());
                hideCommentTextView.setVisibility(View.GONE);
                showMoreTextView.setVisibility(View.GONE);

                if (!item.getCommentId().equals("commentId")) {
                    imageImageView.setVisibility(View.VISIBLE);
                    timeTextView .setVisibility(View.VISIBLE);
                    commentBodyTextView.setVisibility(View.VISIBLE);
                    likeImageView.setVisibility(View.VISIBLE);
                    countLikeTextView.setVisibility(View.VISIBLE);
                    answerTextView.setVisibility(View.VISIBLE);

                    int countAnswer = Integer.parseInt(item.getCommentAnswersCount());
                    if(countAnswer > 0) {
                        boolean isRecyclerVisible = childCommentRecyclerView.getVisibility() == View.VISIBLE;
                        showMoreTextView.setVisibility(isRecyclerVisible ? View.GONE : View.VISIBLE);
                        hideCommentTextView.setVisibility(isRecyclerVisible ? View.VISIBLE : View.GONE);
                    }

                    showMoreTextView.setOnClickListener(v -> {
                        // ?????????????????? ???????????????? ??????????????????????
                        GetCommentListByComment.getCommentListByComment(view.getContext(), prefUtils, item.getCommentId(),
                                childCommentRecyclerView, commentEditText, view);

                        showMoreTextView.setVisibility(View.GONE);
                        hideCommentTextView.setVisibility(View.VISIBLE);
                    });

                    hideCommentTextView.setOnClickListener(v -> {
                        showMoreTextView.setVisibility(View.VISIBLE);
                        hideCommentTextView.setVisibility(View.GONE);
                        childCommentRecyclerView.setVisibility(View.GONE);
                    });

                    Picasso
                            .get()
                            .load(BASE_URL.concat(item.getUrlUserImage()))
                            .into(imageImageView);

                    nameTextView.setText(item.getAuthorName());
                    timeTextView.setText(ShowTimeAgoHelper.showTimeAgo(item.getTime()));

                    final String comment = item.getCommentBody();
                    checkCommentLength(commentBodyTextView, comment, view.getContext()); // ?????????????????? ???????????? ?????????????????????? + ?????????????????? ?????????????? ???? ???????????? "???????? ??????????????????????"

                    boolean isLike = item.getFavoriteFlag().equals("1");
                    changeLikeImage(isLike, likeImageView); // ?????????????????? ???????????????????????? ???????????? ???????????? ?????? ??????

                    countLikeTextView.setText(item.getCountLikeToComment());
                    likeImageClickListener(view.getContext(), likeImageView, item.getCommentId(), isLike); // ?????????????????? ?????????????? ???? ???????????? "?????????????? ????????????"

                    answerClickListener(answerTextView, item.getAuthorName(), item.getCommentId()); // ?????????????????? ?????????????? ???? ???????????? "????????????????"
                } else {
                    nameTextView.setText("");
                    imageImageView.setVisibility(View.GONE);
                    timeTextView .setVisibility(View.GONE);
                    commentBodyTextView.setVisibility(View.GONE);
                    likeImageView.setVisibility(View.GONE);
                    countLikeTextView.setVisibility(View.GONE);
                    answerTextView.setVisibility(View.GONE);
                    childCommentRecyclerView.setVisibility(View.GONE);
                }
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
                if (comment.length() > COUNT_SYMBOL_TO_HIDE_PAR_OF_COMMENT) {
                    SpanText.makeClickableSpanText(commentTextView, comment, context); // ???????????????? ???????????????????????? ?????????? ???????????? + ?????????????????? ??????????????
                } else {
                    commentTextView.setText(comment);
                }
            }
        };
    }
}
