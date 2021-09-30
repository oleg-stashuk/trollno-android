package com.app.trollno.ui.authorisation;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.app.trollno.R;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.Validation;
import com.app.trollno.utils.networking.authorisation.PostLostPassword;

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
            showSnackBarMessage(findViewById(R.id.activity_forgot_password), getString(R.string.mail_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectEmail(email)) {
            emailEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_forgot_password), getString(R.string.uncorrect_email_toast));
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
                    new Thread(() -> PostLostPassword.postLostPassword(this, prefUtils, email, findViewById(R.id.activity_forgot_password))).start();
                }
                hideKeyBoard();
                break;
            case R.id.back_button_forgot_password:
                onBackPressed();
                break;
        }
    }
}