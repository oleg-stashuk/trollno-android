package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.InformationAboutAppDialog;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private TextView accountTextView;
    private TextView nameTextView;
    private TextView loginTextView;
    private Switch darkThemeSwitch;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false; // для обработки нажатия onBackPressed

    private String name = "Иван";
    private String email = "rrrr@gmail.com";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        nameTextView = findViewById(R.id.name_account_profile);
        accountTextView = findViewById(R.id.create_account_profile);
        darkThemeSwitch = findViewById(R.id.switch_theme_profile);
        loginTextView = findViewById(R.id.login_or_exit_account_profile);
        loginTextView.setOnClickListener(this);
        findViewById(R.id.rate_profile).setOnClickListener(this);
        findViewById(R.id.info_profile).setOnClickListener(this);
        findViewById(R.id.tape_button_profile).setOnClickListener(this);
        findViewById(R.id.activity_button_profile).setOnClickListener(this);
        findViewById(R.id.favorites_button_profile).setOnClickListener(this);
        accountTextView.setOnClickListener(this);

        isUserAuthorization = prefsUtils.getIsUserAuthorization();

        makeDarkThemeOnCheckedListener();
        makeIsUserAuthorizationCorrectData();
    }

    private void makeIsUserAuthorizationCorrectData() {
        if(isUserAuthorization) {
            nameTextView.setText(name);
            accountTextView.setText(email);
            loginTextView.setText(getText(R.string.exit));
        } else {
            nameTextView.setText(getText(R.string.hello));
            accountTextView.setText(getText(R.string.create_account));
            loginTextView.setText(getText(R.string.title_login));
        }
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
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.press_twice_to_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_account_profile:
                if(isUserAuthorization) {
                    startActivity(new Intent(this, EditUserProfileActivity.class));
                } else {
                    startActivity(new Intent(this, RegistrationActivity.class));
                }
                finish();
                break;
            case R.id.login_or_exit_account_profile:
                if(isUserAuthorization) {
                    prefsUtils.saveIsUserAuthorization(false);
                }
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.rate_profile:
                showToast("Оцените приложение");
                break;
            case R.id.info_profile:
                InformationAboutAppDialog.aboutDialog(this);
                break;
            case R.id.tape_button_profile: // "Перейти на экран Лента"
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.activity_button_profile: // "Перейти на экран Активность"
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.favorites_button_profile: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
        }
    }
}