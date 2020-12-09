package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText passwordEditText;
    private String name = "";
    private String password = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        nameEditText = findViewById(R.id.edt_name_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);
        findViewById(R.id.login_with_facebook_login).setOnClickListener(this);
        findViewById(R.id.login_with_google_login).setOnClickListener(this);
    }

    private boolean inputFieldIsValid(){
        name = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(name.isEmpty()) {
            nameEditText.requestFocus();
            showToast(getString(R.string.name_is_empty_toast));
            return false;
        }

            if(password.isEmpty()) {
            passwordEditText.requestFocus();
            showToast(getString(R.string.password_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectPassword(password)) {
            passwordEditText.requestFocus();
            showToast(getString(R.string.pasword_length_toast));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TapeActivity.class));
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
                    new Thread(() -> PostUserLogin.postUserLogin(this, name, password, prefUtils)).start();
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