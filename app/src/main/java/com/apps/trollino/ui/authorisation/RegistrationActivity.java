package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class RegistrationActivity extends BaseActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration_button_registration:
                showToast("Зарегистрироваться");
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