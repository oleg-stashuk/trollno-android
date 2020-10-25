package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.InformationAboutAppDialog;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private TextView accountTextView;
    private Switch darkThemeSwitch;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        accountTextView = findViewById(R.id.create_account_profile);
        darkThemeSwitch = findViewById(R.id.switch_theme_profile);
        findViewById(R.id.login_account_profile).setOnClickListener(this);
        findViewById(R.id.term_and_privacy_profile).setOnClickListener(this);
        findViewById(R.id.rate_profile).setOnClickListener(this);
        findViewById(R.id.info_profile).setOnClickListener(this);
        findViewById(R.id.tape_button_profile).setOnClickListener(this);
        findViewById(R.id.activity_button_profile).setOnClickListener(this);
        findViewById(R.id.favorites_button_profile).setOnClickListener(this);
        accountTextView.setOnClickListener(this);

        makeDarkThemeOnCheckedListener();
    }

    // setOnCheckedChangeListener for darkThemeSwitch. If isChecked = true -> dark theme on
    private void makeDarkThemeOnCheckedListener() {
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ProfileActivity.this.showToast(isChecked ? "Dark theme" : "Light theme");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.create_account_profile:
//        startActivity(new Intent(this, RegistrationActivity.class));
        break;
        case R.id.login_account_profile:
        startActivity(new Intent(this, LoginActivity.class));
        break;
        case R.id.term_and_privacy_profile:
//        startActivity(new Intent(this, PrivacyPolicyActivity.class));
        break;
        case R.id.rate_profile:
        showToast("Оцените приложение");
        break;
        case R.id.info_profile:
        InformationAboutAppDialog.aboutDialog(this);
        break;
        case R.id.tape_button_profile: // "Перейти на экран Лента"
        showToast("Перейти на экран Лента");
        break;
        case R.id.activity_button_profile: // "Перейти на экран Активность"
        showToast("Перейти на экран Активность");
        break;
        case R.id.favorites_button_profile: // "Перейти на экран Избранное"
//        startActivity(new Intent(this, FavoriteActivity.class));
//        finish();
        break;
        }
    }
}