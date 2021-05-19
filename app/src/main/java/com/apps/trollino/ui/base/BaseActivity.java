package com.apps.trollino.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.apps.trollino.R;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.CleanSavedDataHelper;
import com.apps.trollino.utils.data.PrefUtils;

public abstract class BaseActivity extends AppCompatActivity {
    protected PrefUtils prefUtils;
    protected final long TIME_TO_UPDATE_DATA = 60 * 1000;  // 1 minute

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        prefUtils = new PrefUtils(getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE));
        initView();
    }

    protected abstract int getLayoutID();

    protected abstract void initView();

//    protected abstract void saveActivityFlag();

    protected void activityFlag(String activityFlag){
        prefUtils.saveCurrentActivity(activityFlag);
    }

    protected void showToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    protected void showSnackBarMessage(View view, String text) {
        SnackBarMessageCustom.showSnackBar(view, text);
    }

    protected void removeAllDataFromDB() {
        CleanSavedDataHelper.cleanBD(this, prefUtils);
    }

    protected void hideKeyBoard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}