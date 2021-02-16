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

        Thread thread = new Thread(() -> {
            try {
                for (startTime = 0; startTime < maxTime; startTime++) {
                    Thread.sleep(15);
                    handler.post(openNextActivity);
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        });
        thread.start();

        new Thread(() -> GetCategoryList.getCategoryList(this, prefUtils, findViewById(R.id.splash_activity))).start();
        new Thread(() -> GetSettings.getSettings(this, prefUtils, null, findViewById(R.id.splash_activity))).start();

        MyFirebaseMessagingService firebaseMessagingService = new MyFirebaseMessagingService();
        firebaseMessagingService.getFireBaseToken();
    }

    Runnable openNextActivity = new Runnable() {
        @Override
        public void run() {
            if (startTime >= maxTime - 1) {
                Bundle extras = getIntent().getExtras();
                if(extras != null) {
                    startActivity(new Intent(SplashActivity.this, ActivityInPostActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, TapeActivity.class));
                }
                finish();
            }
        }
    };
}