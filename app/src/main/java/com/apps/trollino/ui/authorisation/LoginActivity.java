package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.ProfileActivity;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText emailEditText;
    private EditText passwordEditText;
    private String email = "";
    private String password = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        emailEditText = findViewById(R.id.edt_email_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);
        findViewById(R.id.login_with_facebook_login).setOnClickListener(this);
        findViewById(R.id.login_with_google_login).setOnClickListener(this);
    }

    private boolean inputFieldIsValid(){
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
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
            case R.id.forgot_login:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                finish();
                break;
            case R.id.login_button_login:
                if (inputFieldIsValid()) {
                    PostUserLogin.postUserLogin(this, email, password, prefUtils);
                }
                break;
            case R.id.register_login:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.login_with_facebook_login:
                showToast("Войти через Facebook");
                break;
            case R.id.login_with_google_login:
                showToast("Войти через Google");
                break;
        }
    }
}