package com.apps.trollino.ui.main_group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.RequestBlockUserModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.networking.GetSettings;
import com.apps.trollino.utils.networking.authorisation.GetUserProfile;
import com.apps.trollino.utils.networking.user.BlockUserProfile;

import java.util.ArrayList;
import java.util.List;

public class EditUserProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_edit_user_profile;
    }

    @Override
    protected void initView() {
        nameEditText = findViewById(R.id.name_edit_user_profile);
        emailEditText = findViewById(R.id.email_edit_user_profile);
        passwordEditText = findViewById(R.id.password_edit_user_profile);
        imageView = findViewById(R.id.image_edit_user_profile);
        imageView.setOnClickListener(this);
        findViewById(R.id.back_button_edit_user_profile).setOnClickListener(this);
        findViewById(R.id.delete_button_edit_user_profile).setOnClickListener(this);
        findViewById(R.id.update_button_edit_user_profile).setOnClickListener(this);

        new Thread(() -> GetUserProfile.getUserProfile(this, prefUtils, imageView, nameEditText, emailEditText)).start();
        passwordEditText.setText(prefUtils.getPassword());
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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage(getResources().getString(R.string.do_you_really_want_to_delete_your_account))
                .setNegativeButton("Нет", (dialog1, which) -> {
                    Log.d("OkHttp", "Нет");
                    dialog1.cancel();
                })
                .setPositiveButton("Удалить", (dialog, which) -> {
                    Log.d("OkHttp", "Удалить");
                    List<RequestBlockUserModel.StatusBlockUserModel> blockStatus = new ArrayList<>();
                    blockStatus.add(new RequestBlockUserModel.StatusBlockUserModel(false));
                    RequestBlockUserModel requestBlockUserModel = new RequestBlockUserModel(blockStatus);

                    new Thread(() -> BlockUserProfile.blockUserProfile(this, prefUtils, requestBlockUserModel)).start();
                    dialog.cancel();
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                break;
            case R.id.image_edit_user_profile:
                new Thread(() -> GetSettings.getSettings(this, prefUtils, imageView)).start();
                break;
            case R.id.update_button_edit_user_profile:
                showToast("Обновить профиль");
                break;
        }
    }
}