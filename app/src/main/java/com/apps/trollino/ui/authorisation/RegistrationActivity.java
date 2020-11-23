package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.ProfileActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.apps.trollino.utils.Validation;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String name = "";
    private String email = "";
    private String password = "";


    @Override
    protected int getLayoutID() {
        return R.layout.activity_registration;
    }

    @Override
    protected void initView() {
        nameEditText = findViewById(R.id.edt_name_registration);
        emailEditText = findViewById(R.id.edt_email_registration);
        passwordEditText = findViewById(R.id.edt_password_registration);

        findViewById(R.id.registration_button_registration).setOnClickListener(this);
        findViewById(R.id.login_registration).setOnClickListener(this);
        findViewById(R.id.register_with_facebook_registration).setOnClickListener(this);
        findViewById(R.id.register_with_google_registration).setOnClickListener(this);
    }

    private boolean inputFieldIsValid(){
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        name = nameEditText.getText().toString();

        if(name.isEmpty()) {
            nameEditText.requestFocus();
            showToast("Поле Имя не должно быть пустым");
            return false;
        }
        if(email.isEmpty()) {
            emailEditText.requestFocus();
            showToast("Поле email не должно быть пустым");
            return false;
        } else if(!Validation.isCorrectEmail(email)) {
            emailEditText.requestFocus();
            showToast("Некоректный email");
            return false;
        }

        if(password.isEmpty()) {
            passwordEditText.requestFocus();
            showToast("Поле пароль не должно быть пустым");
            return false;
        } else if(!Validation.isCorrectPassword(password)) {
            passwordEditText.requestFocus();
            showToast("Поле пароль долджно содержать больше 6 символов");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ProfileActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration_button_registration:
                if(inputFieldIsValid()){
                    prefUtils.saveIsUserAuthorization(true);
                    startActivity(new Intent(this, TapeActivity.class));
                    finish();
                }
                break;
            case R.id.login_registration:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.register_with_facebook_registration:
                showToast("Зарегистрироваться через Facebook");
                break;
            case R.id.register_with_google_registration:
                showToast("Зарегистрироваться через Google");
                break;
        }
    }
}