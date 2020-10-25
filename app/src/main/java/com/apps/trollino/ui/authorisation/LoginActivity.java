package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText email;
    private EditText password;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        email = findViewById(R.id.edt_email_login);
        password = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_login:
//                startActivity(new Intent(this, ForgotPasswordActivity.class));
//                finish();
                break;
            case R.id.login_button_login:
                showToast("LOGIN");
                break;
            case R.id.register_login:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
        }
    }
}