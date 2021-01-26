package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.LoginWithFacebook;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import static com.apps.trollino.utils.Const.LOG_TAG;

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
        activityLayout = findViewById(R.id.activity_login);
        facebookLoginButton = findViewById(R.id.facebook_button);
        facebookLoginButton.setOnClickListener(this);
        nameEditText = findViewById(R.id.edt_name_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);

        initToolbar();

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogin() {
        facebookLoginButton.setPermissions(Arrays.asList("public_profile", "email"));
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginWithFacebook.loginWithFacebook(LoginActivity.this, prefUtils,
                        loginResult.getAccessToken().getToken(), activityLayout);
                Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!!! Login with facebook is success");
            }

            @Override
            public void onCancel() {
                Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!!! onCancel");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!!! error - " + error.getLocalizedMessage());

            }
        });

    }

    private boolean inputFieldIsValid(){
        name = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();
        if(name.isEmpty()) {
            nameEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_login), getString(R.string.name_is_empty_toast));
            return false;
        }

        if(password.isEmpty()) {
            passwordEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_login), getString(R.string.password_is_empty_toast));
            return false;
        } else if(!Validation.isCorrectPassword(password)) {
            passwordEditText.requestFocus();
            showSnackBarMessage(findViewById(R.id.activity_login), getString(R.string.pasword_length_toast));
            return false;
        }
        return true;
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.title_autorisation);

        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(false);; // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
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
