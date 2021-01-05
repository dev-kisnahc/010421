package com.kisnahc.a0104_21login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    //naverlogin
    private static final String CLIENT_ID = "aecYqKzQfK_5kXYHIMHg";
    private static final String CLIENT_SECRET = "fTfEMeqoKh";
    private static final String CLIENT_NAME = "0104_21login";

    OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //kakaoLogin
        Function2<OAuthToken, Throwable, Unit> function2 = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null) {
                    Log.i(TAG, "invoke: oAuthToken is not null");

                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            Log.i(TAG, "invoke: id = "+ user.getId());
                            Log.i(TAG, "invoke: email = "+user.getKakaoAccount());
                            //Toast.makeText(LoginActivity.this, "success:" + user.getKakaoAccount(), Toast.LENGTH_LONG).show();

                            redirectSignupActivity();
                            return null;
                        }
                    });
                } else if (throwable != null) {
                    Log.w(TAG, "invoke: oAuth id " + throwable);
                }
                return null;
            }
        };

        findViewById(R.id.btn_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    LoginClient.getInstance().loginWithKakaoTalk(LoginActivity.this, function2);
                } else {
                    LoginClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);
                }
            }
        });

        //naverLogin
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(LoginActivity.this, CLIENT_ID, CLIENT_SECRET, CLIENT_NAME);

        findViewById(R.id.btn_naver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {

                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = mOAuthLoginModule.getAccessToken(LoginActivity.this);
                            String refreshToken = mOAuthLoginModule.refreshAccessToken(LoginActivity.this);
                            Long expiresAt = mOAuthLoginModule.getExpiresAt(LoginActivity.this);
                            String tokenType = mOAuthLoginModule.getTokenType(LoginActivity.this);

                            redirectSignupActivity();
                            finish();

                        } else {
                            String errorCode = mOAuthLoginModule.getLastErrorCode(LoginActivity.this).getCode();
                            String errorDesc = mOAuthLoginModule.getLastErrorDesc(LoginActivity.this);
                            Toast.makeText(LoginActivity.this, "errorCode :" + errorCode + ", errorDesc :" + errorDesc, Toast.LENGTH_LONG).show();
                            Log.w(TAG, "errorCode : " + errorCode + "errorDesc :" +errorDesc);
                        }
                    }
                };
                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
            }
        });
    }


    protected void redirectSignupActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}