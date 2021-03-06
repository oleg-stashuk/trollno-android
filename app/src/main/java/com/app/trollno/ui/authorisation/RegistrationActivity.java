package com.app.trollno.ui.authorisation;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.app.trollno.R;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.OpenActivityHelper;
import com.app.trollno.utils.Validation;
import com.app.trollno.utils.data.CleanSavedDataHelper;
import com.app.trollno.utils.networking.authorisation.PostUserRegistration;

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
        initToolbar();

        nameEditText = findViewById(R.id.edt_name_registration);
        emailEditText = findViewById(R.id.edt_email_registration);
        passwordEditText = findViewById(R.id.edt_password_registration);

        findViewById(R.id.registration_button_registration).setOnClickListener(this);
        findViewById(R.id.login_registration).setOnClickListener(this);
    }

    private boolean inputFieldIsValid(){
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();
        name = nameEditText.getText().toString();

        if(name.isEmpty()) {
            nameEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_registration), getString(R.string.name_is_empty_toast));
            return false;
        }
        if(email.isEmpty()) {
            emailEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_registration), getString(R.string.mail_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectEmail(email)) {
            emailEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_registration), getString(R.string.uncorrect_email_toast));
            return false;
        }

        if(password.isEmpty()) {
            passwordEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_registration), getString(R.string.password_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectPassword(password)) {
            passwordEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_registration), getString(R.string.pasword_length_toast));
            return false;
        }
        return true;
    }


    // ?????????????????????? Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_registration);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_registration);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ???????????????????? ???????????? BackPress
            getSupportActionBar().setHomeButtonEnabled(true);; // ?????????????????? ???? ???????????????????? ????????????????????
            getSupportActionBar().setDisplayShowTitleEnabled(true); // ???????????????????? ??????????????????
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
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
                    hideKeyBoard();
                    CleanSavedDataHelper.cleanAllDataIfUserRemoveOrLogout(prefUtils);
                    new Thread(() -> PostUserRegistration.postRegistration(this, name, email, password, prefUtils, findViewById(R.id.activity_registration))).start();
                }
                break;
            case R.id.login_registration:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}