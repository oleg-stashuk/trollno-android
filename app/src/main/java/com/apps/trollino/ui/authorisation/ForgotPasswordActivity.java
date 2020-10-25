package com.apps.trollino.ui.authorisation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText emailEditText;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_button_forgot_password:
                showToast("Отправить пароль на почту");
                break;
            case R.id.back_button_forgot_password:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}