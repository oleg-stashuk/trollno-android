package com.apps.trollino.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.single_post.ItemPostModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.YoutubeActivity;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.ImageViewDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class OnePostElementAdapter extends BaseRecyclerAdapter<ItemPostModel.MediaBlock> {
    private final int RECOVERY_REQUEST = 1;
    private final String adMobId;
    private final String bannerId;
    private final int blockCountFromApi;
    private int blockCount = 1;
    private final int itemsSize;

    public OnePostElementAdapter(BaseActivity baseActivity, List<ItemPostModel.MediaBlock> items, PrefUtils prefUtils) {
        super(baseActivity, items);
        this.blockCountFromApi = prefUtils.getCountBetweenAds();
        this.itemsSize = items.size();
        this.adMobId = prefUtils.getAdMobId();
        this.bannerId = prefUtils.getBannerId();
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
                RelativeLayout adMobViewRelativeLayout = view.findViewById(R.id.ad_mob_view_element_of_post);
                LinearLayout adLinearLayout = view.findViewById(R.id.ad_block_element_of_post);

                TextView titleTextView = view.findViewById(R.id.title_element_of_post);
                ImageView imageView = view.findViewById(R.id.image_element_of_post);
                TextView sourceTextView = view.findViewById(R.id.source_text_element_of_post);
                TextView sourceLinkTextView = view.findViewById(R.id.link_source_element_of_post);


                TextView youtubeTextView = view.findViewById(R.id.youtube_element_of_post);
                YouTubeThumbnailView youTubeThumbnailView = view.findViewById(R.id.youtube_view_1);
                ImageView videoImageView = view.findViewById(R.id.image_video_element_of_post);

                TextView descriptionTextView = view.findViewById(R.id.description_element_of_post);


                ItemPostModel.EntityMediaBlock entityItem = item.getEntity();

                if (entityItem.getTitle().isEmpty()) {
                    titleTextView.setVisibility(View.GONE);
                } else {
                    titleTextView.setVisibility(View.VISIBLE);
                    titleTextView.setText(entityItem.getTitle());
                }

                makeImageBlock(entityItem.getImage(), imageView, sourceTextView, sourceLinkTextView);
                makeYoutubeBlock(entityItem.getYoutube(), youtubeTextView, youTubeThumbnailView, videoImageView);

                if (entityItem.getDesc().isEmpty()) {
                    descriptionTextView.setVisibility(View.GONE);
                } else {
                    descriptionTextView.setVisibility(View.VISIBLE);
                    descriptionTextView.setText(entityItem.getDesc());
                }

                showAdBlock(adMobViewRelativeLayout, adLinearLayout, view.getContext());// show advertising
            }

            private void showAdBlock(RelativeLayout adViewRelativeLayout, LinearLayout adLinearLayout, Context context) {
                if(blockCountFromApi < itemsSize) {
                    if(blockCount == blockCountFromApi) {
                        getAdBlock(adViewRelativeLayout, adLinearLayout, context);
                        blockCount = 1;
                    } else {
                        adLinearLayout.setVisibility(View.GONE);
                        blockCount++;
                    }
                } else {
                    if(blockCount == itemsSize) {
                        getAdBlock(adViewRelativeLayout, adLinearLayout, context);
                    } else {
                        adLinearLayout.setVisibility(View.GONE);
                        blockCount++;
                    }
                }
            }

            private void getAdBlock(RelativeLayout adViewRelativeLayout, LinearLayout adLinearLayout, Context context) {
                try {
                    ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    Bundle bundle = ai.metaData;
                    String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    Log.d(TAG_LOG, "Name Found: " + myApiKey);
                    ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", adMobId);//you can replace your key APPLICATION_ID here
                    String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    Log.d(TAG_LOG, "ReNamed Found: " + ApiKey);
                    adLinearLayout.setVisibility(View.VISIBLE);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.d(TAG_LOG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                } catch (NullPointerException e) {
                    Log.d(TAG_LOG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                }

                AdView mAdView = new AdView(context);
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(bannerId);
                adViewRelativeLayout.addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

            private void makeImageBlock(ItemPostModel.ImageBlock image, ImageView imageView, TextView sourceTextView, TextView sourceLinkTextView) {
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
                        if (! image.getResource().isEmpty() && image.getResource().contains("http")) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(image.getResource()));
                            view.getContext().startActivity(browserIntent);
                        }
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
            }

            private void makeYoutubeBlock(String youtubeLink, TextView youtubeTextView,
                                          YouTubeThumbnailView youTubeThumbnailView, ImageView videoImageView) {
                if (youtubeLink.isEmpty()) {
                    youTubeThumbnailView.setVisibility(View.GONE);
                    youtubeTextView.setVisibility(View.GONE);
                    videoImageView.setVisibility(View.GONE);
                } else {
                    int length = youtubeLink.length();
                    String videoAddress = youtubeLink.substring(length-11, length);

                    youTubeThumbnailView.initialize(Const.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                            youTubeThumbnailLoader.setVideo(videoAddress);
                        }

                        @Override
                        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                            if (youTubeInitializationResult.isUserRecoverableError()) {
                                youTubeInitializationResult.getErrorDialog((Activity) view.getContext(), RECOVERY_REQUEST).show();
                            } else {
                                String error = String.format(view.getContext().getString(R.string.player_error), youTubeInitializationResult.toString());
                                Toast.makeText(view.getContext(), error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    youtubeTextView.setVisibility(View.VISIBLE);
                    videoImageView.setVisibility(View.VISIBLE);
                    youTubeThumbnailView.setVisibility(View.VISIBLE);

                    youTubeThumbnailView.setOnClickListener(v -> {
                        openYoutubeVideo(videoAddress);
                    });

                    videoImageView.setOnClickListener(view1 -> {
                        openYoutubeVideo(videoAddress);
                    });
                }
            }

            private void openYoutubeVideo(String videoAddress) {
                Intent intent = new Intent(view.getContext(), YoutubeActivity.class);
                intent.putExtra(YoutubeActivity.YOUTUBE_VIDEO_LINK, videoAddress);
                view.getContext().startActivity(intent);
            }
        };


    }
}
