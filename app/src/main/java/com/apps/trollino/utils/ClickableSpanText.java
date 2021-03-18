package com.apps.trollino.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.utils.data.Const;

public class ClickableSpanText {

    public static void makeClickableSpanText (final TextView textViewWithClickablePart, final String comment, Context context) {
        String seeAllComment = context.getString(R.string.see_all_comment);
        SpannableString comment100 = new SpannableString(comment.substring(0, Const.COUNT_SYMBOL_TO_HIDE_PAR_OF_COMMENT).concat("... ").concat(seeAllComment));

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

        int start = comment100.length() - seeAllComment.length();
        int finish = comment100.length();
        comment100.setSpan(clickableSpan, start, finish, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textViewWithClickablePart.setText(comment100);
        textViewWithClickablePart.setMovementMethod(LinkMovementMethod.getInstance());
        textViewWithClickablePart.setHighlightColor(Color.TRANSPARENT);
    }

}
