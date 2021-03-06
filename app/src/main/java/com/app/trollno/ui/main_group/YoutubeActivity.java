package com.app.trollno.ui.main_group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.trollno.R;
import com.app.trollno.utils.data.PrefUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    public static String YOUTUBE_VIDEO_LINK = "YOUTUBE_VIDEO_LINK";
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private PrefUtils prefUtils;

    private String videoAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        prefUtils = new PrefUtils(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));

        videoAddress = this.getIntent().getStringExtra(YOUTUBE_VIDEO_LINK);

        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(prefUtils.getYoutubeId(), this);

        ImageView closeImageView = findViewById(R.id.close_button_youtube);
        closeImageView.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.cueVideo(videoAddress);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(prefUtils.getYoutubeId(), this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
}