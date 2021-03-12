package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.apps.trollino.R;
import com.apps.trollino.service.MyFirebaseMessagingService;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.networking.authorisation.GetUserProfile;
import com.apps.trollino.utils.networking.authorisation.PostLogout;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.switchmaterial.SwitchMaterial;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout userIncludeLinearLayout;
    private ShimmerFrameLayout userIncludeShimmer;
    private LinearLayout guestIncludeLinearLayout;
    private LinearLayout bottomNavigation;
    private ImageView userImageView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button exitButton;
    private ImageView indicatorImageView;

    private boolean doubleBackToExitPressedOnce = false; // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        userIncludeLinearLayout = findViewById(R.id.include_user_profile);
        userIncludeShimmer = findViewById(R.id.include_user_profile_shimmer);
        guestIncludeLinearLayout = findViewById(R.id.include_user_not_authorization_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation_profile);
        userImageView = findViewById(R.id.image_account_profile_include);
        nameTextView = findViewById(R.id.name_account_profile_include);
        emailTextView = findViewById(R.id.email_account_profile_include);
        exitButton = findViewById(R.id.exit_button_profile);
        TextView profileBottomNavigationTextView = findViewById(R.id.profile_button);
        indicatorImageView = findViewById(R.id.indicator_image);

        SwitchMaterial markReadPostSwitch = findViewById(R.id.mark_read_post_switch);
        SwitchMaterial answerToCommentSwitch = findViewById(R.id.answer_to_comment_switch);

        findViewById(R.id.login_button_include_profile_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_profile_for_guest).setOnClickListener(this);
        exitButton.setOnClickListener(this);
        userIncludeLinearLayout.setOnClickListener(this);
        findViewById(R.id.rate_profile).setOnClickListener(this);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        emailTextView.setOnClickListener(this);

        profileBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_green, 0, 0);
        profileBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        prefUtils.saveCurrentActivity(OpenActivityHelper.PROFILE_ACTIVITY);

        makeIsUserAuthorizationCorrectData();

        markReadPostSwitch.setChecked(prefUtils.isShowReadPost());
        markReadPostSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefUtils.saveIsShowReadPost(isChecked);
            //@TODO add PUT to update this data in API
        });

        answerToCommentSwitch.setChecked(prefUtils.isSendPushAboutAnswerToComment());
        answerToCommentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefUtils.saveIsSendPushAboutAnswerToComment(isChecked);
            //@TODO add PUT to update this data in API and add or removeFireBaseToken()
        });
    }

    private void makeIsUserAuthorizationCorrectData() {
        if(prefUtils.getIsUserAuthorization()) {
            userIncludeLinearLayout.setVisibility(View.GONE);
            userIncludeShimmer.setVisibility(View.VISIBLE);
            guestIncludeLinearLayout.setVisibility(View.GONE);
            exitButton.setVisibility(View.VISIBLE);

            new Thread(() -> {
                GetUserProfile.getUserProfile(this, prefUtils, userImageView, nameTextView, emailTextView,
                        bottomNavigation, userIncludeLinearLayout, userIncludeShimmer);
            }).start();

            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
        } else {
            userIncludeLinearLayout.setVisibility(View.GONE);
            userIncludeShimmer.setVisibility(View.GONE);
            guestIncludeLinearLayout.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.GONE);
        }
    }

    private void openPlayMarketForRateTheApp() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException exc) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void removeFireBaseToken() {
        MyFirebaseMessagingService fireBaseService = new MyFirebaseMessagingService();
        fireBaseService.onDeletedFireBaseToken(this, prefUtils);
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCurrentActivity("");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarOnTheTopByBottomNavigation(findViewById(R.id.activity_profile), getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
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
                LoginManager.getInstance().logOut();
                new Thread(
                        () -> PostLogout.postLogout(this, prefUtils, bottomNavigation)
                ).start();
                removeFireBaseToken();
                break;
            case R.id.registration_button_include_profile_for_guest:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.rate_profile:
                openPlayMarketForRateTheApp();
                break;
            case R.id.tape_button: // "Перейти на экран Лента"
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.activity_button: // "Перейти на экран Активность"
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.favorites_button: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
        }
    }
}