package com.app.trollno.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.app.trollno.R;
import com.app.trollno.service.MyFirebaseMessagingService;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.ui.main_group.AnswersActivity;
import com.app.trollno.ui.main_group.TapeActivity;
import com.app.trollno.utils.data.CleanSavedDataHelper;
import com.app.trollno.utils.networking.GetCategoryList;
import com.app.trollno.utils.networking.GetSettings;

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
        removeAllDataFromDB();
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int density  = (int) getResources().getDisplayMetrics().density;

        int widthForImageForOneColumn = (int) (displayMetrics.widthPixels * 0.4); // 40% of screen
        prefUtils.saveImageWidthForOneColumn(widthForImageForOneColumn);
        int widthForImageForTwoColumn = displayMetrics.widthPixels / 2 - (density * 24);
        prefUtils.saveImageWidth(widthForImageForTwoColumn);

        if(!prefUtils.getIsUserAuthorization()) {
            CleanSavedDataHelper.cleanAllDataIfUserRemoveOrLogout(prefUtils);
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
            startActivity(new Intent(SplashActivity.this, AnswersActivity.class));
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