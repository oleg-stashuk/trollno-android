package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        findViewById(R.id.back_button_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_search:
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
        }
    }
}