package com.apps.trollino.ui.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.trollino.R;
import com.apps.trollino.utils.data.PrefUtils;

public abstract class BaseActivity extends AppCompatActivity {
    protected PrefUtils prefUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        prefUtils = new PrefUtils(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
        initView();
    }

    protected abstract int getLayoutID();

    protected abstract void initView();

    protected void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

}