package com.apps.trollino.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.squareup.picasso.Picasso;

public class ImageViewDialog {

    public void showDialog(Context context, String msg, String imageUrl){
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_image_view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);

        TextView text = dialog.findViewById(R.id.text_dialog_view);
        text.setText(msg);

        ImageButton closeButton = dialog.findViewById(R.id.close_button_dialog_view);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        ImageButton shareButton = dialog.findViewById(R.id.share_button_dialog_view);
        shareButton.setOnClickListener(v -> {
            Toast.makeText(context,"SHARE" ,Toast.LENGTH_SHORT).show();
        });


        ImageView image = dialog.findViewById(R.id.image_dialog_view);
        try {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.icon_facebook)
                    .into(image);
            Log.d("OkHttp", "try");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("OkHttp", "catch");
        }

        dialog.show();
    }

}