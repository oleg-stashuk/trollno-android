package com.app.trollno.ui.main_group;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.app.trollno.R;
import com.app.trollno.data.model.profile.RequestBlockUserModel;
import com.app.trollno.service.MyFirebaseMessagingService;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.OpenActivityHelper;
import com.app.trollno.utils.Validation;
import com.app.trollno.utils.networking.GetSettings;
import com.app.trollno.utils.networking.authorisation.GetUserProfile;
import com.app.trollno.utils.networking.user.BlockUserProfile;
import com.app.trollno.utils.networking.user.UpdatePassword;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textfield.TextInputLayout;

public class EditUserProfileActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout editUserLayout;
    ShimmerFrameLayout editUserShimmer;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView emailTextView;
    private EditText passwordEditText;
    private Button updateButton;

    private String password = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_user_profile;
    }

    @Override
    protected void initView() {
        initToolbar();

        editUserLayout = findViewById(R.id.edit_user_profile_layout);
        editUserShimmer = findViewById(R.id.edit_user_profile_shimmer);
        nameTextView = findViewById(R.id.name_edit_user_profile);
        emailTextView = findViewById(R.id.email_edit_user_profile);
        passwordEditText = findViewById(R.id.password_edit_user_profile);
        TextInputLayout passwordBlock = findViewById(R.id.password_block_edit_user_profile);
        imageView = findViewById(R.id.image_edit_user_profile);
        TextView loginByFacebook = findViewById(R.id.login_by_facebook);
        imageView.setOnClickListener(this);
        updateButton = findViewById(R.id.update_button_edit_user_profile);
        updateButton.setOnClickListener(this);

        loginByFacebook.setVisibility(prefUtils.isUserLoginByFacebook() ? View.VISIBLE : View.GONE);
        updateButton.setVisibility(prefUtils.isUserLoginByFacebook() ? View.GONE : View.VISIBLE);
        passwordBlock.setVisibility(prefUtils.isUserLoginByFacebook() ? View.GONE : View.VISIBLE);

        updateButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyBackgroundVideo));
        password = prefUtils.getPassword();
        passwordEditText.setText(password);
        prefUtils.saveCurrentActivity(OpenActivityHelper.EDIT_PROFILE_ACTIVITY);

        new Thread(() -> GetUserProfile.getUserProfile(this, prefUtils, imageView, nameTextView, emailTextView,
                findViewById(R.id.activity_edit_user_profile), editUserLayout, editUserShimmer)).start();
        passwordTextChangedListener();
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_edit_profile);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.edit_profile));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(true); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
    }

    // Добавить Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Обрабтка нажантия на выпадающий список из Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.delete_button:
                showDeleteConfirmationDialog();
                break;
        }
        return true;
    }

    private void passwordTextChangedListener() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = s.toString();
                if(password.equals(newPassword) || newPassword.isEmpty() || !Validation.isCorrectPassword(newPassword)) {
                    updateButton.setEnabled(false);
                    updateButton.setBackgroundColor(ContextCompat.getColor(EditUserProfileActivity.this, R.color.colorGreyBackgroundVideo));
                } else {
                    updateButton.setEnabled(true);
                    updateButton.setBackgroundResource(R.drawable.button_background);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(getResources().getString(R.string.do_you_really_want_to_delete_your_account))
                .setNegativeButton(this.getString(R.string.no_button_for_dialog), (dialog1, which) -> {
                    dialog1.cancel();
                })
                .setPositiveButton(R.string.remove_account_confirm_button, (dialog, which) -> {
                    MyFirebaseMessagingService fireBaseService = new MyFirebaseMessagingService();
                    fireBaseService.onDeletedFireBaseToken(this, prefUtils);
                    new Thread(() -> BlockUserProfile.blockUserProfile(this, prefUtils, new RequestBlockUserModel(), findViewById(R.id.activity_edit_user_profile))).start();
                    dialog.cancel();
                });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_edit_user_profile:
                new Thread(() -> GetSettings.getSettings(this, prefUtils, imageView, findViewById(R.id.activity_edit_user_profile))).start();
                break;
            case R.id.update_button_edit_user_profile:
                new Thread(() ->
                        UpdatePassword.updatePassword(this, prefUtils, password,
                                passwordEditText.getText().toString(), findViewById(R.id.activity_edit_user_profile))).start();
                break;
        }
    }
}