package com.apps.trollino.ui.authorisation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.Validation;
import com.apps.trollino.utils.networking.authorisation.LoginWithFacebook;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.CustomTabActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;


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
        facebookLoginButton = findViewById(R.id.login_button);
        facebookLoginButton.setOnClickListener(this);
        nameEditText = findViewById(R.id.edt_name_login);
        passwordEditText = findViewById(R.id.edt_password_login);
        findViewById(R.id.forgot_login).setOnClickListener(this);
        findViewById(R.id.login_button_login).setOnClickListener(this);
        findViewById(R.id.register_login).setOnClickListener(this);
        findViewById(R.id.facebook_button_layout).setOnClickListener(this);
        findViewById(R.id.google_button_layout).setOnClickListener(this);

        initToolbar();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! accessToken 1111 - " + accessToken);
        Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! isLoggedIn 1111 - " + isLoggedIn);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setPermissions(Arrays.asList("public_profile", "email"));
//        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
//                String accessTokenFromFacebook = loginResult.getAccessToken().getToken();
//                new Thread(
//                        () -> LoginWithFacebook.loginWithFacebook(LoginActivity.this, prefUtils, accessTokenFromFacebook, activityLayout))
//                        .start();
//
//
//
////                GraphRequest request = GraphRequest.newMeRequest(
////                        loginResult.getAccessToken(),
////                        (object, response) -> {
////                            JSONObject json = response.getJSONObject();
////                            try {
////                                if (json != null) {
////                                    String name=json.getString("name");
////                                    String email= json.getString("email");
////                                    Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! ------ name - " + name + " " + email);
////                                }
////
////                            } catch (JSONException e) {
////                                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! ------ catch - " + e.getLocalizedMessage());
////                            }
////
////
////
////                            Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! response 3333 - " + response.toString());
////
////                            // Application code
////                            try {
////                                String name = object.getString("name");
////                                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! name - " + name);
////                            } catch (JSONException e) {
////                                e.printStackTrace();
////                                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! error 3333 - " + e.getLocalizedMessage());
////                            }
////
////                            try {
////                                String email = object.getString("email");
////                                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! email - " + email);
////                            } catch (JSONException e) {
//////                                AccessToken.getCurrentAccessToken().getPermissions();
////                                LoginManager.getInstance().logOut();
////                                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! error 3333333 - " + e.getLocalizedMessage());
////                            }
////                        });
////                Bundle parameters = new Bundle();
////                parameters.putString("fields", "id,name,email");
////                request.setParameters(parameters);
////                request.executeAsync();
//
//
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! onCancel 555555");
//                LoginManager.getInstance().logOut();
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                LoginManager.getInstance().logOut();
//                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! error 444444 - " + error.getLocalizedMessage());
//
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! accessToken 222 - " + accessToken);
        Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! isLoggedIn 222 - " + isLoggedIn);
        Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! onActivityResult " + requestCode);
//        String accessToken = AccessToken.getCurrentAccessToken().getToken();
//        new Thread(
//                () -> LoginWithFacebook.loginWithFacebook(LoginActivity.this, prefUtils, accessToken, activityLayout))
//                .start();
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void facebookLogin() {
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                String accessTokenFromFacebook = loginResult.getAccessToken().getToken();
//                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                new Thread(
                        () -> LoginWithFacebook.loginWithFacebook(LoginActivity.this, prefUtils, accessTokenFromFacebook, activityLayout))
                        .start();
            }

            @Override
            public void onCancel() {
                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! onCancel 555555");
                LoginManager.getInstance().logOut();
            }

            @Override
            public void onError(FacebookException error) {
                LoginManager.getInstance().logOut();
                Log.d("OkHttp_1", "!!!!!!!!!!!!!!!!!!! error 444444 - " + error.getLocalizedMessage());

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
                    new Thread(() -> PostUserLogin.postUserLogin(this, name, password, prefUtils, activityLayout)).start();
                }
                break;
            case R.id.register_login:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.facebook_button_layout:
                startActivity(new Intent(this, CustomTabActivity.class));
                showSnackBarMessage(activityLayout, "Войти через Facebook");
                break;
            case R.id.google_button_layout:
                showSnackBarMessage(activityLayout, "Войти через Google");
                break;
            case R.id.login_button:
                facebookLogin();
                break;
        }
    }

}



/*
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
        findViewById(R.id.facebook_button_layout).setOnClickListener(this);
        findViewById(R.id.google_button_layout).setOnClickListener(this);

        initToolbar();
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
                    new Thread(() -> PostUserLogin.postUserLogin(this, name, password, prefUtils, findViewById(R.id.activity_login))).start();
                }
                break;
            case R.id.register_login:
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
            case R.id.facebook_button_layout:
                startActivity(new Intent(this, CustomTabActivity.class));
                showSnackBarMessage(findViewById(R.id.activity_login), "Войти через Facebook");
                break;
            case R.id.google_button_layout:
                showSnackBarMessage(findViewById(R.id.activity_login), "Войти через Google");
                break;
        }
    }

}
*/