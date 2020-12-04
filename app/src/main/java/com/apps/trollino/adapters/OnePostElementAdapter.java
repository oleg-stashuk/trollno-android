package com.apps.trollino.adapters;

import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.ImageViewDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.networking.GetTikTok.getTikTok;

public class OnePostElementAdapter extends BaseRecyclerAdapter<ItemPostModel.MediaBlock> {

    public OnePostElementAdapter(BaseActivity baseActivity, List<ItemPostModel.MediaBlock> items) {
        super(baseActivity, items);
    }

    @Override
    protected int getCardLayoutID() {
        return R.layout.item_element_of_post;
    }

    @Override
    protected BaseItem createViewHolder(final View view) {
        return new BaseItem(view) {
            @Override
            public void bind(ItemPostModel.MediaBlock item) {
                TextView titleTextView = view.findViewById(R.id.title_element_of_post);
                ImageView imageView = view.findViewById(R.id.image_element_of_post);
                TextView sourceTextView = view.findViewById(R.id.source_text_element_of_post);
                TextView sourceLinkTextView = view.findViewById(R.id.link_source_element_of_post);
                LinearLayout instagramLinearLayout = view.findViewById(R.id.instagram_item_element_of_post);
                TextView instagramTextView = view.findViewById(R.id.instagram_element_of_post);

                LinearLayout youtubeLinearLayout = view.findViewById(R.id.youtube_item_element_of_post);
                TextView youtubeTextView = view.findViewById(R.id.youtube_element_of_post);

                ImageView tikTokImageView = view.findViewById(R.id.tiktok_image_item_element_of_post);

                TextView descriptionTextView = view.findViewById(R.id.description_element_of_post);


                ItemPostModel.EntityMediaBlock entityItem = item.getEntity();
                if (entityItem.getTitle().isEmpty()) {
                    titleTextView.setVisibility(View.GONE);
                } else {
                    titleTextView.setVisibility(View.VISIBLE);
                    titleTextView.setText(entityItem.getTitle());
                }

                ItemPostModel.ImageBlock image = entityItem.getImage();
                if (image.getUrlImage().isEmpty()) {
                    imageView.setVisibility(View.GONE);
                    sourceTextView.setVisibility(View.GONE);
                    sourceLinkTextView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    sourceTextView.setVisibility(View.VISIBLE);
                    sourceLinkTextView.setVisibility(View.VISIBLE);

                    SpannableString content = new SpannableString(image.getResourceTitle());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    sourceLinkTextView.setText(content);
                    sourceLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(image.getResource()));
                        view.getContext().startActivity(browserIntent);
                    });

                    Picasso
                            .get()
                            .load(image.getUrlImage())
                            .into(imageView);

                    imageView.setOnClickListener(v -> {
                        ImageViewDialog dialog = new ImageViewDialog();
                        dialog.showDialog(view.getContext(), image.getResourceTitle(), image.getUrlImage());
                    });
                }

                if (entityItem.getInstagram().isEmpty()) {
                    instagramLinearLayout.setVisibility(View.GONE);
                    instagramTextView.setVisibility(View.GONE);
                } else {
                    instagramLinearLayout.setVisibility(View.VISIBLE);
                    instagramTextView.setVisibility(View.VISIBLE);
                    instagramTextView.setText(entityItem.getInstagram());
                    instagramLinearLayout.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entityItem.getInstagram()));
                        view.getContext().startActivity(browserIntent);
                    });
                }

                if (entityItem.getYoutube().isEmpty()) {
                    youtubeLinearLayout.setVisibility(View.GONE);
                    youtubeTextView.setVisibility(View.GONE);
                } else {
                    youtubeLinearLayout.setVisibility(View.VISIBLE);
                    youtubeTextView.setVisibility(View.VISIBLE);
                    youtubeTextView.setText(entityItem.getYoutube());
                    youtubeLinearLayout.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entityItem.getYoutube()));
                        view.getContext().startActivity(browserIntent);
                    });
                }

                if (entityItem.getTiktok().isEmpty()) {
                    tikTokImageView.setVisibility(View.GONE);
                } else {
                    tikTokImageView.setVisibility(View.VISIBLE);
                    getTikTok(view.getContext(), tikTokImageView, entityItem.getTiktok());

//                    tikTokImageView.setOnClickListener(v -> {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(entityItem.getTiktok()));
//                        view.getContext().startActivity(browserIntent);
//                    });
                }

                if (entityItem.getDesc().isEmpty()) {
                    descriptionTextView.setVisibility(View.GONE);
                } else {
                    descriptionTextView.setVisibility(View.VISIBLE);
                    descriptionTextView.setText(entityItem.getDesc());
                }

            }

        };


    }
}
