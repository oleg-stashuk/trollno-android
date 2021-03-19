 package com.apps.trollino.ui.main_group;

 import android.content.Intent;
 import android.os.Handler;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import androidx.appcompat.widget.Toolbar;
 import androidx.core.content.ContextCompat;
 import androidx.recyclerview.widget.RecyclerView;

 import com.apps.trollino.R;
 import com.apps.trollino.ui.authorisation.LoginActivity;
 import com.apps.trollino.ui.authorisation.RegistrationActivity;
 import com.apps.trollino.ui.base.BaseActivity;
 import com.apps.trollino.utils.OpenActivityHelper;
 import com.apps.trollino.utils.data.CommentListToUserActivityFromApi;
 import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
 import com.apps.trollino.utils.recycler.MakeRecyclerViewForCommentToUserActivity;
 import com.facebook.shimmer.ShimmerFrameLayout;
 import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
 import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

 import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;

 public class ActivityInPostActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView postWithActivityRecyclerView;
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout refreshLayout;
    private LinearLayout bottomNavigation;

    private View includeNoDataForUser;
    private TextView noDataTextView;
    private View userAuthorizationView;
     private ImageView indicatorImageView;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_in_post;
    }

    @Override
    protected void initView() {
        initToolbar();

        shimmer = findViewById(R.id.include_user_comments_shimmer);
        refreshLayout = findViewById(R.id.refresh_layout_activity_in_post);
        bottomNavigation = findViewById(R.id.bottom_navigation_activity);
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_activity_in_post);
        includeNoDataForUser = findViewById(R.id.include_no_data_for_user_activity_in_post);
        noDataTextView = findViewById(R.id.txt_include_no_data);
        postWithActivityRecyclerView = findViewById(R.id.recycler_activity_in_post);
        TextView activityBottomNavigationTextView = findViewById(R.id.activity_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);
        findViewById(R.id.login_button_include_activity_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_activity_for_guest).setOnClickListener(this);

        activityBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_activity_green, 0, 0);
        activityBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        isUserAuthorization = prefUtils.getIsUserAuthorization();

        prefUtils.saveCurrentActivity(OpenActivityHelper.ACTIVITY_USER_ACTIVITY);

        CommentListToUserActivityFromApi.getInstance().removeAllDataFromList(prefUtils); // при загрузке активити почистить сохраненные данные для инфинитискрол и текущую страницу для загрузки с АПИ
        checkUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        updateCommentBySwipe();
    }

    private void checkUserAuthorization() {
        if(isUserAuthorization) {
            shimmer.setVisibility(View.VISIBLE);
            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
            userAuthorizationView.setVisibility(View.GONE);
            postWithActivityRecyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);

            getDataFromApi(shimmer, null, true);
        } else {
            shimmer.setVisibility(View.GONE);
            userAuthorizationView.setVisibility(View.VISIBLE);
            postWithActivityRecyclerView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

     private void updateCommentBySwipe() {
         refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
         refreshLayout.setOnRefreshListener(direction -> {
             getDataFromApi(null, refreshLayout, (direction == SwipyRefreshLayoutDirection.TOP));
         });
     }

    private void getDataFromApi(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean isNewData) {
        MakeRecyclerViewForCommentToUserActivity
                .makeRecyclerViewForCommentToUserActivity(this, prefUtils, postWithActivityRecyclerView,
                        shimmerToApi, refreshLayoutToApi, includeNoDataForUser , noDataTextView, bottomNavigation, isNewData);
    }



    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_in_post);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(false); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentActivity("");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarOnTheTopByBottomNavigation(bottomNavigation, getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tape_button: // "Перейти на экран Лента"
                prefUtils.saveCommentIdForActivity("");
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.favorites_button: // "Перейти на экран Избранное"
                prefUtils.saveCommentIdForActivity("");
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button: // "Перейти на экран Профиль"
                prefUtils.saveCommentIdForActivity("");
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
            case R.id.login_button_include_activity_for_guest: // "Перейти на экран Авторизации"
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.registration_button_include_activity_for_guest: // "Перейти на экран Регистрации"
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
        }
    }
}