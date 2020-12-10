package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.PostLostPassword;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText emailEditText;
    private String email;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void initView() {
        emailEditText = findViewById(R.id.edt_email_forgot_password);
        findViewById(R.id.send_button_forgot_password).setOnClickListener(this);
        findViewById(R.id.back_button_forgot_password).setOnClickListener(this);
    }

    private boolean inputFieldIsValid(){
        email = emailEditText.getText().toString();

        if(email.isEmpty()) {
            emailEditText.requestFocus();
            showToast(getString(R.string.mail_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectEmail(email)) {
            emailEditText.requestFocus();
            showToast(getString(R.string.uncorrect_email_toast));
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button_forgot_password:
                if (inputFieldIsValid()) {
                    new Thread(() -> PostLostPassword.postLostPassword(this, email)).start();
                }
                break;
            case R.id.back_button_forgot_password:
                onBackPressed();
                break;
        }
    }
}