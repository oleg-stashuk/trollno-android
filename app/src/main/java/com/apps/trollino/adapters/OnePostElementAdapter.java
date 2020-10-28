package com.apps.trollino.adapters;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.PostModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class OnePostElementAdapter extends BaseRecyclerAdapter<PostModel.OneElementPost> {

    public OnePostElementAdapter(BaseActivity baseActivity, List<PostModel.OneElementPost> items) {
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
            public void bind(PostModel.OneElementPost item) {
                TextView titleTextView = view.findViewById(R.id.title_element_of_post);
                ImageView imageView = view.findViewById(R.id.image_element_of_post);
                TextView sourceTextView = view.findViewById(R.id.source_text_element_of_post);
                TextView sourceLinkTextView = view.findViewById(R.id.link_source_element_of_post);
                LinearLayout videoLinearLayout = view.findViewById(R.id.video_item_element_of_post);
                TextView descriptionTextView = view.findViewById(R.id.description_element_of_post);


                if(item.getTitleElement().isEmpty()) {
                    titleTextView.setVisibility(View.GONE);
                } else {
                    titleTextView.setVisibility(View.VISIBLE);
                    titleTextView.setText(item.getTitleElement());
                }
                if(item.getLinkImageElement().isEmpty()) {
                    imageView.setVisibility(View.GONE);
                    sourceTextView.setVisibility(View.GONE);
                    sourceLinkTextView.setVisibility(View.GONE);
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    sourceTextView.setVisibility(View.VISIBLE);
                    sourceLinkTextView.setVisibility(View.VISIBLE);

                    SpannableString content = new SpannableString(item.getLinkResourceElement());
                    content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                    sourceLinkTextView.setText(content);
                    imageView.setImageResource(R.drawable.cookie_icon);
                }
                if(item.getLinkVideoElement().isEmpty()) {
                    videoLinearLayout.setVisibility(View.GONE);
                } else {
                    videoLinearLayout.setVisibility(View.VISIBLE);
                }
                if(item.getDescriptionElement().isEmpty()) {
                    descriptionTextView.setVisibility(View.GONE);
                } else {
                    descriptionTextView.setVisibility(View.VISIBLE);
                    descriptionTextView.setText(item.getDescriptionElement());
                }

            }
        };
    }
}
