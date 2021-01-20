 package com.apps.trollino.ui.main_group;

 import android.content.Intent;
 import android.os.Handler;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.TextView;

 import androidx.appcompat.app.ActionBar;
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

 public class ActivityInPostActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView postWithActivityRecyclerView;

    private View includeNoDataForUser;
    private TextView noDataTextView;
    private View userAuthorizationView;
     private TextView activityBottomNavigationTextView;
     private ImageView indicatorImageView;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_in_post;
    }

    @Override
    protected void initView() {
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_activity_in_post);
        includeNoDataForUser = findViewById(R.id.include_no_data_for_user_activity_in_post);
        noDataTextView = findViewById(R.id.txt_include_no_data);
        postWithActivityRecyclerView = findViewById(R.id.recycler_activity_in_post);
        activityBottomNavigationTextView = findViewById(R.id.activity_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);
        findViewById(R.id.login_button_include_activity_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_activity_for_guest).setOnClickListener(this);

        activityBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_activity_green, 0, 0);
        activityBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        isUserAuthorization = prefUtils.getIsUserAuthorization();
        if(isUserAuthorization) {
            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
        }

        prefUtils.saveCurrentActivity(OpenActivityHelper.ACTIVITY_USER_ACTIVITY);

        CommentListToUserActivityFromApi.getInstance().removeAllDataFromList(prefUtils); // при загрузке активити почистить сохраненные данные для инфинитискрол и текущую страницу для загрузки с АПИ
        checkUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        initToolbar();
    }

    private void checkUserAuthorization() {
        if(isUserAuthorization) {
            userAuthorizationView.setVisibility(View.GONE);
            postWithActivityRecyclerView.setVisibility(View.VISIBLE);

            MakeRecyclerViewForCommentToUserActivity
                    .makeRecyclerViewForCommentToUserActivity(this, prefUtils, postWithActivityRecyclerView, includeNoDataForUser , noDataTextView, findViewById(R.id.activity_in_post));
        } else {
            userAuthorizationView.setVisibility(View.VISIBLE);
            postWithActivityRecyclerView.setVisibility(View.GONE);
        }
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_in_post);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.activity));

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(false); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
    }

//    // Добавить Menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if(isUserAuthorization) {
//            getMenuInflater().inflate(R.menu.post_with_activity_menu, menu);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    // Обрабтка нажатия на выпадающий список из Menu
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.mark_all_as_read_menu) {
//            showSnackBarMessage(findViewById(R.id.activity_in_post), getString(R.string.mark_all_as_read));
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentActivity("");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarMessage(findViewById(R.id.activity_in_post), getString(R.string.press_twice_to_exit));
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