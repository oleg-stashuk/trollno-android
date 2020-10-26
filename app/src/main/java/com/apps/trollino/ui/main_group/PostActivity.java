package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class PostActivity extends BaseActivity implements View.OnClickListener{


    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        findViewById(R.id.back_button_post_activity).setOnClickListener(this);
        findViewById(R.id.favorite_button_post_activity).setOnClickListener(this);
        findViewById(R.id.comment_button_post_activity).setOnClickListener(this);
        findViewById(R.id.share_button_post_activity).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_post_activity:
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.favorite_button_post_activity:
                showToast("Добавить в избранное");
                break;
            case R.id.comment_button_post_activity:
                showToast("Добавить комментарий");
                break;
            case R.id.share_button_post_activity:
                showToast("Поделиться");
                break;
        }
    }
}