package com.apps.trollino.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.utils.OnSwipeTouchListener;
import com.squareup.picasso.Picasso;

import static com.apps.trollino.utils.Const.LOG_TAG;

public class ImageViewDialog {

    public void showDialog(Context context, String msg, String imageUrl, String imageResource){
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_image_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);

        RelativeLayout relativeLayout = dialog.findViewById(R.id.custom_dialog_image_view);
        makeTouchListener(dialog, relativeLayout);

        TextView text = dialog.findViewById(R.id.text_dialog_view);
        text.setText(msg);

        ImageButton closeButton = dialog.findViewById(R.id.close_button_dialog_view);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        ImageButton shareButton = dialog.findViewById(R.id.share_button_dialog_view);
        shareButton.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, dialog.getContext().getResources().getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, imageResource);
                dialog.getContext().startActivity(Intent.createChooser(shareIntent, ""));
            } catch(Exception e) {
                Log.d(LOG_TAG, "!!!!!!!!!!!! " + e.getLocalizedMessage());
            }

        });

        ImageView image = dialog.findViewById(R.id.image_dialog_view);
        try {
            Picasso.get()
                    .load(imageUrl)
                    .into(image);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog1.cancel();
                return true;
            }
            return false;
        });

        dialog.show();
    }


    // Действия при свайпах в разные стороны
    private void makeTouchListener(final Dialog dialog, RelativeLayout relativeLayout) {
        relativeLayout.setOnTouchListener(new OnSwipeTouchListener(dialog.getContext()) {
            public void onSwipeTop() {
                dialog.dismiss();
            }

            public void onSwipeRight() {
            }

            public void onSwipeLeft() {
            }

            public void onSwipeBottom() {
                dialog.dismiss();
            }

        });
    }

}
