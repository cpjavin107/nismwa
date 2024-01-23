package com.javinindia.nismwa.notification;


import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.javinindia.nismwa.preference.SharedPreferencesManager;


public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(refreshedToken)) {
            SharedPreferencesManager.setDeviceToken(getApplicationContext(), refreshedToken);
        } else {
        }
    }
}