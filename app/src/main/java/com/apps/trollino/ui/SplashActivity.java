package com.apps.trollino.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.apps.trollino.R;
import com.apps.trollino.service.MyFirebaseMessagingService;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.ActivityInPostActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.apps.trollino.utils.data.CleanSavedDataHelper;
import com.apps.trollino.utils.networking.GetCategoryList;
import com.apps.trollino.utils.networking.GetSettings;

public class SplashActivity extends BaseActivity {
    private Handler handler;
    private int startTime;
    private final int maxTime = 150;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        handler = new Handler();
        CleanSavedDataHelper.cleanAllDataFromApi(prefUtils);
        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentActivity("");

        new Thread(() -> {
            try {
                for (startTime = 0; startTime < maxTime; startTime++) {
                    Thread.sleep(15);
                    handler.post(openNextActivity);
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }).start();

        new Thread(() -> GetCategoryList.getCategoryList(this, prefUtils, findViewById(R.id.splash_activity))).start();
        new Thread(() -> GetSettings.getSettings(this, prefUtils, null, findViewById(R.id.splash_activity))).start();

        if (prefUtils.getIsUserAuthorization()) {
            MyFirebaseMessagingService firebaseMessagingService = new MyFirebaseMessagingService();
            firebaseMessagingService.getFireBaseToken(this, prefUtils);
        }
    }

    Runnable openNextActivity = new Runnable() {
        @Override
        public void run() {
            if (startTime >= maxTime - 1) {
                Bundle extras = getIntent().getExtras();
                if(extras != null) {
                    choiceNextActivity(!isNotification(extras));
                } else {
                    choiceNextActivity(true);
                }
                finish();
            }
        }
    };

    private void choiceNextActivity(boolean isTapeActivity) {
        if(isTapeActivity) {
            startActivity(new Intent(SplashActivity.this, TapeActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, ActivityInPostActivity.class));
        }
    }

    private boolean isNotification(Bundle extras) {
        String from = String.valueOf(extras);
        if(from.contains("ParcelledData")){
            int part = Integer.parseInt(from.substring(31, from.length()-1));
            return part > 100;
        } else {
            int part = extras.toString().length();
            return part > 50;
        }
    }
}