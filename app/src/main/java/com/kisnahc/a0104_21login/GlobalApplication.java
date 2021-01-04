package com.kisnahc.a0104_21login;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "e39c6cbb12c3993c36f661c183f34fd7");
    }
}
