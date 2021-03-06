 package com.app.trollno.ui.main_group;

 import android.content.Intent;
 import android.os.Handler;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.ProgressBar;
 import android.widget.TextView;

 import androidx.appcompat.widget.Toolbar;
 import androidx.core.content.ContextCompat;
 import androidx.recyclerview.widget.RecyclerView;

 import com.app.trollno.R;
 import com.app.trollno.ui.authorisation.LoginActivity;
 import com.app.trollno.ui.authorisation.RegistrationActivity;
 import com.app.trollno.ui.base.BaseActivity;
 import com.app.trollno.utils.OpenActivityHelper;
 import com.app.trollno.utils.data.AnswersFromApi;
 import com.app.trollno.utils.networking.user_action.GetNewAnswersCount;
 import com.facebook.shimmer.ShimmerFrameLayout;
 import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

 import static com.app.trollno.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;
 import static com.app.trollno.utils.recycler.MakeRecyclerViewForAnswerActivity.makeRecyclerViewForCommentToUserActivity;

 public class AnswersActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView answersRecyclerView;
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout refreshLayout;
    private LinearLayout bottomNavigation;
    private ProgressBar progressBar;

    private View includeNoDataForUser;
    private TextView noDataTextView;
    private View userAuthorizationView;
     private ImageView indicatorImageView;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_answers;
    }

    @Override
    protected void initView() {
        initToolbar();

        shimmer = findViewById(R.id.include_user_comments_shimmer);
        refreshLayout = findViewById(R.id.refresh_layout_for_answers);
        bottomNavigation = findViewById(R.id.bottom_navigation_activity);
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_for_answers);
        includeNoDataForUser = findViewById(R.id.include_no_data_for_user_for_answers);
        noDataTextView = findViewById(R.id.txt_include_no_data);
        answersRecyclerView = findViewById(R.id.recycler_for_answers);
        TextView activityBottomNavigationTextView = findViewById(R.id.activity_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        progressBar = findViewById(R.id.progress_bar_answer);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);
        findViewById(R.id.login_button_include_activity_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_activity_for_guest).setOnClickListener(this);

        activityBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_activity_green, 0, 0);
        activityBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        isUserAuthorization = prefUtils.getIsUserAuthorization();

        prefUtils.saveCurrentActivity(OpenActivityHelper.ANSWERS_ACTIVITY);

        checkUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        updateCommentBySwipe();
    }


     @Override
     protected void onResume() {
         super.onResume();
         if (isUserAuthorization) {
             getAnswersCount();

             Handler handler = new Handler();
             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     getAnswersCount();
                     handler.postDelayed(this, TIME_TO_UPDATE_DATA);
                 }
             }, TIME_TO_UPDATE_DATA);
         }
     }

     private void getAnswersCount() {
         new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
     }

    private void checkUserAuthorization() {
        if(isUserAuthorization) {
            userAuthorizationView.setVisibility(View.GONE);
            answersRecyclerView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);

            int AnswerListSize = AnswersFromApi.getInstance().getListSize();
            shimmer.setVisibility(AnswerListSize < 1 ? View.VISIBLE : View.GONE);
            getDataFromApi(shimmer, null);
        } else {
            shimmer.setVisibility(View.GONE);
            userAuthorizationView.setVisibility(View.VISIBLE);
            answersRecyclerView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
        }
    }

     private void updateCommentBySwipe() {
         refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
         refreshLayout.setOnRefreshListener(direction -> {
             getDataFromApi(null, refreshLayout);
             answersRecyclerView.suppressLayout(true);
         });
     }

    private void getDataFromApi(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi) {
        makeRecyclerViewForCommentToUserActivity(this, prefUtils, answersRecyclerView,
                shimmerToApi, refreshLayoutToApi, includeNoDataForUser , noDataTextView,
                bottomNavigation, progressBar);
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
            removeAllDataFromDB();
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
