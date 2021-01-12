package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.dialogs.InformationAboutAppDialog;
import com.apps.trollino.utils.networking.authorisation.GetUserProfile;
import com.apps.trollino.utils.networking.authorisation.PostLogout;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout userIncludeLinearLayout;
    private LinearLayout guestIncludeLinearLayout;
    private ImageView userImageView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Switch darkThemeSwitch;
    private Button exitButton;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false; // для обработки нажатия onBackPressed


    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        userIncludeLinearLayout = findViewById(R.id.include_user_profile);
        guestIncludeLinearLayout = findViewById(R.id.include_user_not_authorization_profile);
        userImageView = findViewById(R.id.image_account_profile_include);
        nameTextView = findViewById(R.id.name_account_profile_include);
        emailTextView = findViewById(R.id.email_account_profile_include);
        darkThemeSwitch = findViewById(R.id.switch_theme_profile);
        findViewById(R.id.login_button_include_profile_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_profile_for_guest).setOnClickListener(this);
        exitButton = findViewById(R.id.exit_button_profile);
        exitButton.setOnClickListener(this);

        userIncludeLinearLayout.setOnClickListener(this);
        findViewById(R.id.rate_profile).setOnClickListener(this);
        findViewById(R.id.info_profile).setOnClickListener(this);
        findViewById(R.id.tape_button_profile).setOnClickListener(this);
        findViewById(R.id.activity_button_profile).setOnClickListener(this);
        findViewById(R.id.favorites_button_profile).setOnClickListener(this);
        emailTextView.setOnClickListener(this);

        isUserAuthorization = prefUtils.getIsUserAuthorization();
        prefUtils.saveCurrentActivity(OpenActivityHelper.PROFILE_ACTIVITY);

        makeDarkThemeOnCheckedListener();
        makeIsUserAuthorizationCorrectData();
    }

    private void makeIsUserAuthorizationCorrectData() {
        if(isUserAuthorization) {
            userIncludeLinearLayout.setVisibility(View.VISIBLE);
            guestIncludeLinearLayout.setVisibility(View.GONE);
            exitButton.setVisibility(View.VISIBLE);

            new Thread(() -> {
                GetUserProfile.getUserProfile(this, prefUtils, userImageView, nameTextView, emailTextView);
            }).start();
        } else {
            userIncludeLinearLayout.setVisibility(View.GONE);
            guestIncludeLinearLayout.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.GONE);
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

    private void openPlayMarketForRateTheApp() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCurrentActivity("");
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
            case R.id.include_user_profile:
                startActivity(new Intent(this, EditUserProfileActivity.class));
                finish();
                break;
            case R.id.login_button_include_profile_for_guest:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.exit_button_profile:
                new Thread(() -> PostLogout.postLogout(this, prefUtils)).start();
                break;
            case R.id.registration_button_include_profile_for_guest:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.rate_profile:
                openPlayMarketForRateTheApp();
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