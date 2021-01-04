package com.kisnahc.a0104_21login;

import androidx.appcompat.app.AppCompatActivity;

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

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    //OAuthLogin mOAuthLoginModule;

//    private static final String CLIENT_ID = "aecYqKzQfK_5kXYHIMHg";
//    private static final String CLIENT_SECRET = "fTfEMeqoKh";
//    private static final String CLIENT_NAME = "010421login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //kakao Login
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

                            redirectSignupActivity();
                            return null;
                        }
                    });
                }else if (throwable != null) {
                    Log.w(TAG, "invoke: oAuth id "+throwable);
                }
                return null;
            }
        };

        findViewById(R.id.btn_kakao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    LoginClient.getInstance().loginWithKakaoTalk(LoginActivity.this, function2);
                }else {
                    LoginClient.getInstance().loginWithKakaoAccount(LoginActivity.this, function2);
                }
            }
        });


    }

    protected void redirectSignupActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}