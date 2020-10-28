package com.apps.trollino.ui.main_group;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class EditUserProfileActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imageView;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private String name = "Иван";
    private String email = "test@gmail.com";
    private String password = "123456";

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

        nameEditText.setText(name);
        emailEditText.setText(email);
        passwordEditText.setText(password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_edit_user_profile:
                onBackPressed();
                break;
            case R.id.delete_button_edit_user_profile:
                showToast("Удалить профиль");
                break;
            case R.id.image_edit_user_profile:
                showToast("Изменить картинку");
                break;
            case R.id.update_button_edit_user_profile:
                showToast("Обновить профиль");
                break;
        }
    }
}