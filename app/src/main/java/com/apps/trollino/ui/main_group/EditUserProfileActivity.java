package com.apps.trollino.ui.main_group;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.GetSettings;
import com.apps.trollino.utils.networking.authorisation.GetUserProfile;
import com.apps.trollino.utils.networking.user.UpdatePassword;

public class EditUserProfileActivity extends BaseActivity implements View.OnClickListener {
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
        nameTextView = findViewById(R.id.name_edit_user_profile);
        emailTextView = findViewById(R.id.email_edit_user_profile);
        passwordEditText = findViewById(R.id.password_edit_user_profile);
        imageView = findViewById(R.id.image_edit_user_profile);
        imageView.setOnClickListener(this);
        findViewById(R.id.back_button_edit_user_profile).setOnClickListener(this);
        findViewById(R.id.delete_button_edit_user_profile).setOnClickListener(this);
        updateButton = findViewById(R.id.update_button_edit_user_profile);
        updateButton.setOnClickListener(this);
        updateButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreyBackgroundVideo));

        password = prefUtils.getPassword();
        passwordEditText.setText(password);

        new Thread(() -> GetUserProfile.getUserProfile(this, prefUtils, imageView, nameTextView, emailTextView, findViewById(R.id.activity_edit_user_profile))).start();
        passwordTextChangedListener();
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
                    Log.d("OkHttp", "Нет");
                    dialog1.cancel();
                })
                .setPositiveButton(R.string.remove_account_confirm_button, (dialog, which) -> {
                    Log.d("OkHttp", "Удалить");
                    showSnackBarMessage(findViewById(R.id.activity_edit_user_profile), "Удалить профиль");
//                    List<RequestBlockUserModel.StatusBlockUserModel> blockStatus = new ArrayList<>();
//                    blockStatus.add(new RequestBlockUserModel.StatusBlockUserModel(false));
//                    RequestBlockUserModel requestBlockUserModel = new RequestBlockUserModel(blockStatus);
//                    new Thread(() -> BlockUserProfile.blockUserProfile(this, prefUtils, requestBlockUserModel)).start();
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
            case R.id.back_button_edit_user_profile:
                onBackPressed();
                break;
            case R.id.delete_button_edit_user_profile:
                showDeleteConfirmationDialog();
                break;
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