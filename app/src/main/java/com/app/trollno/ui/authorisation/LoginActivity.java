package com.app.trollno.ui.authorisation;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.app.trollno.R;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.OpenActivityHelper;
import com.app.trollno.utils.Validation;
import com.app.trollno.utils.data.Const;
import com.app.trollno.utils.networking.authorisation.LoginWithFacebook;
import com.app.trollno.utils.networking.authorisation.PostUserLogin;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText nameEditText;
    private EditText passwordEditText;
    private String name = "";
    private String password = "";
    private LoginButton facebookLoginButton;
    private CallbackManager callbackManager;
    private LinearLayout activityLayout;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initToolbar();

        activityLayout = findViewById(R.id.login_activity_liner_layout);
        facebookLoginButton = findViewById(R.id.facebook_button);
        facebookLoginButton.setOnClickListener(this);
        nameEditText = findViewById(R.id.edt_name_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogin() {
        facebookLoginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        facebookLoginButton.setPermissions(Arrays.asList("public_profile", "email"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginWithFacebook.loginWithFacebook(LoginActivity.this, prefUtils,
                        loginResult.getAccessToken().getToken(), activityLayout);
                Log.d(Const.TAG_LOG, "!!!!!!!!!!!!!!!!!!! Login with facebook is success");
            }

            @Override
            public void onCancel() {
                Log.d(Const.TAG_LOG, "!!!!!!!!!!!!!!!!!!! onCancel");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                Log.d(Const.TAG_LOG, "!!!!!!!!!!!!!!!!!!! error - " + error.getLocalizedMessage());

            }
        });

    }

    private boolean inputFieldIsValid(){
        name = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(name.isEmpty()) {
            nameEditText.requestFocus();
            showSnackBarMessage(activityLayout, getString(R.string.name_is_empty_toast));
            return false;
        }

        if(password.isEmpty()) {
            passwordEditText.requestFocus();
            showSnackBarMessage(activityLayout, getString(R.string.password_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectPassword(password)) {
            passwordEditText.requestFocus();
            showSnackBarMessage(activityLayout, getString(R.string.pasword_length_toast));
            return false;
        }
        return true;
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_autorisation);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(true); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
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
            case R.id.forgot_login:
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                finish();
                break;
            case R.id.login_button_login:
                if (inputFieldIsValid()) {
                    hideKeyBoard();
                    new Thread(
                            () -> PostUserLogin.postUserLogin(this, name, password, prefUtils, activityLayout)
                    ).start();
                }
                break;
            case R.id.register_login:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.facebook_button:
                facebookLogin();
                break;
        }
    }

}
