package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.PostUserRegistration;

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
            showToast(getString(R.string.name_is_empty_toast));
            return false;
        }
        if(email.isEmpty()) {
            emailEditText.requestFocus();
            showToast(getString(R.string.mail_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectEmail(email)) {
            emailEditText.requestFocus();
            showToast(getString(R.string.uncorrect_email_toast));
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
        startActivity(OpenActivityHelper.openActivity(this, prefUtils));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registration_button_registration:
                if(inputFieldIsValid()){
                    new Thread(() -> PostUserRegistration.postRegistration(this, name, email, password, prefUtils)).start();
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