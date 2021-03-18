package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.apps.trollino.R;
import com.apps.trollino.databinding.ActivityProfileBinding;
import com.apps.trollino.service.MyFirebaseMessagingService;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.networking.authorisation.GetUserProfile;
import com.apps.trollino.utils.networking.authorisation.PostLogout;
import com.apps.trollino.utils.networking.user.UpdateUserProfileSettings;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.facebook.login.LoginManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;

public class ProfileActivity extends BaseActivity implements View.OnClickListener{
    private ActivityProfileBinding binding;

    private LinearLayout userIncludeLinearLayout;
    private ShimmerFrameLayout userIncludeShimmer;
    private LinearLayout guestIncludeLinearLayout;
    private LinearLayout bottomNavigation;
    private ImageView userImageView;
    private TextView emailTextView;
    private TextView nameTextView;
    private ImageView indicatorImageView;

    private boolean doubleBackToExitPressedOnce = false; // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initView() {
        initToolbar();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        prefUtils.saveCurrentActivity(OpenActivityHelper.PROFILE_ACTIVITY);

        userIncludeLinearLayout = findViewById(R.id.include_user_profile);
        userIncludeShimmer = findViewById(R.id.include_user_profile_shimmer);
        guestIncludeLinearLayout = findViewById(R.id.include_user_not_authorization_profile);
        bottomNavigation = findViewById(R.id.bottom_navigation_profile);
        userImageView = findViewById(R.id.image_account_profile_include);
        nameTextView = findViewById(R.id.name_account_profile_include);
        emailTextView = findViewById(R.id.email_account_profile_include);
        TextView profileBottomNavigationTextView = findViewById(R.id.profile_button);
        indicatorImageView = findViewById(R.id.indicator_image);

        findViewById(R.id.login_button_include_profile_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_profile_for_guest).setOnClickListener(this);
        userIncludeLinearLayout.setOnClickListener(this);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        emailTextView.setOnClickListener(this);

        profileBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_profile_green, 0, 0);
        profileBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        makeIsUserAuthorizationCorrectData();
        initSwitch();
        initClickListeners();
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final MaterialToolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_profile));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(false); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
    }

    private void makeIsUserAuthorizationCorrectData() {
        if(prefUtils.getIsUserAuthorization()) {
            userIncludeLinearLayout.setVisibility(View.GONE);
            userIncludeShimmer.setVisibility(View.VISIBLE);
            guestIncludeLinearLayout.setVisibility(View.GONE);
            binding.exitButtonProfile.setVisibility(View.VISIBLE);
            binding.markReadPostSwitch.setVisibility(View.VISIBLE);
            binding.answerToCommentSwitch.setVisibility(View.VISIBLE);

            new Thread(() -> {
                GetUserProfile.getUserProfile(this, prefUtils, userImageView, nameTextView, emailTextView,
                        bottomNavigation, userIncludeLinearLayout, userIncludeShimmer);
            }).start();

            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
        } else {
            userIncludeLinearLayout.setVisibility(View.GONE);
            userIncludeShimmer.setVisibility(View.GONE);
            guestIncludeLinearLayout.setVisibility(View.VISIBLE);
            binding.exitButtonProfile.setVisibility(View.GONE);
            binding.markReadPostSwitch.setVisibility(View.GONE);
            binding.answerToCommentSwitch.setVisibility(View.GONE);
        }
    }

    private void initSwitch(){
        binding.markReadPostSwitch.setChecked(prefUtils.isShowReadPost());
        binding.markReadPostSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefUtils.saveIsShowReadPost(isChecked);
            UpdateUserProfileSettings.updateShowReadPosts(this, prefUtils, isChecked);
        });


        binding.answerToCommentSwitch.setChecked(prefUtils.isSendPushAboutAnswerToComment());
        binding.answerToCommentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefUtils.saveIsSendPushAboutAnswerToComment(isChecked);
            UpdateUserProfileSettings.updateSendPushNewAnswers(this, prefUtils, isChecked);
        });
    }

    private void initClickListeners() {
        binding.rateProfile.setOnClickListener(v -> {
            openPlayMarketForRateTheApp();
        });

        binding.exitButtonProfile.setOnClickListener(v -> {
            LoginManager.getInstance().logOut();
            new Thread(
                    () -> PostLogout.postLogout(this, prefUtils, bottomNavigation)
            ).start();
            removeFireBaseToken();
        });

        binding.includeUserProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditUserProfileActivity.class));
            finish();
        });
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
            case R.id.login_button_include_profile_for_guest:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.registration_button_include_profile_for_guest:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
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