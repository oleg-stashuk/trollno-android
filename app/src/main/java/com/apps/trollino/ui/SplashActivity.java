package com.apps.trollino.ui;

import android.content.Intent;
import android.os.Handler;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.ProfileActivity;
import com.apps.trollino.ui.main_group.TapeActivity;

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
        prefsUtils.saveNewPostCurrentPage(0);
        prefsUtils.saveNewPostTotalPage(0);
        Thread thread = new Thread(() -> {
            try {
                for (startTime = 0; startTime < maxTime; startTime++) {
                    Thread.sleep(10);
                    handler.post(openNextActivity);
                }
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        });
        thread.start();
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