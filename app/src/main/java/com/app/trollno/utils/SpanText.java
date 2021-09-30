package com.app.trollno.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.app.trollno.R;
import com.app.trollno.utils.data.Const;

public class SpanText {

    public static void makeClickableSpanText (final TextView textViewWithClickablePart, final String comment, Context context) {
        String seeAllComment = context.getString(R.string.see_all_comment);
        SpannableString spannableString = new SpannableString(comment.substring(0, Const.COUNT_SYMBOL_TO_HIDE_PAR_OF_COMMENT).concat("... ").concat(seeAllComment));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                textViewWithClickablePart.setText(comment);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int start = spannableString.length() - seeAllComment.length();
        int finish = spannableString.length();
        spannableString.setSpan(clickableSpan, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewWithClickablePart.setText(spannableString);
        textViewWithClickablePart.setMovementMethod(LinkMovementMethod.getInstance());
        textViewWithClickablePart.setHighlightColor(Color.TRANSPARENT);
    }

    public static void makeClickableSpanTextWithSpanName (final TextView textViewWithClickablePart, final String comment, Context context) {
        String seeAllComment = context.getString(R.string.see_all_comment);

        int indexNameFinish = comment.indexOf(",");
        String name = "@".concat(comment.substring(0, indexNameFinish));
        String newComment = name.concat(comment.substring(indexNameFinish+1));

        SpannableString spannableString =
                new SpannableString(newComment.substring(0, Const.COUNT_SYMBOL_TO_HIDE_PAR_OF_COMMENT)
                        .concat("... ").concat(seeAllComment));

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                makeSpanName(textViewWithClickablePart, comment, context);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)),
                0, indexNameFinish+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int start = spannableString.length() - seeAllComment.length();
        int finish = spannableString.length();
        spannableString.setSpan(clickableSpan, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewWithClickablePart.setText(spannableString);
        textViewWithClickablePart.setMovementMethod(LinkMovementMethod.getInstance());
        textViewWithClickablePart.setHighlightColor(Color.TRANSPARENT);
    }

    // Выделить имя
    public static void makeSpanName(final TextView textViewWithSpanName, final String comment, Context context) {
        int indexNameFinish = comment.indexOf(",");
        String name = "@".concat(comment.substring(0, indexNameFinish));
        String newComment = name.concat(comment.substring(indexNameFinish+1));

        Spannable nameToSpan = new SpannableString(newComment);
        nameToSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorPrimary)),
                0, indexNameFinish+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewWithSpanName.setText(nameToSpan);
    }

}
