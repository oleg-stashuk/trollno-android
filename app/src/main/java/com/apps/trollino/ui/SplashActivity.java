package com.apps.trollino.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.apps.trollino.MyFirebaseMessagingService;
import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.apps.trollino.utils.data.CleanSavedDataHelper;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.networking.GetCategoryList;
import com.apps.trollino.utils.networking.GetSettings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

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

        Boolean extras = getIntent().getBooleanExtra(MyFirebaseMessagingService.NOTIFICATION, false);
        if (extras != null) {
//            boolean fff = extras.getBoolean(MyFirebaseMessagingService.NOTIFICATION);
            Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!! extras " + extras.toString());
        }else{
//            boolean fff = extras.getBoolean(MyFirebaseMessagingService.NOTIFICATION);
            Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!! extras else " + extras.toString());

        }

        MyFirebaseMessagingService firebaseMessagingService = new MyFirebaseMessagingService();
        firebaseMessagingService.getFireBaseToken();
    }

    Runnable openNextActivity = new Runnable() {
        @Override
        public void run() {
            if (startTime >= maxTime - 1) {
                startActivity(new Intent(SplashActivity.this, TapeActivity.class));
                finish();
            }
        }
    };
}