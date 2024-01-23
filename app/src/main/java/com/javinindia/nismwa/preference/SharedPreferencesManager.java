package com.javinindia.nismwa.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.javinindia.nismwa.constant.Constants;


public class SharedPreferencesManager {

    private SharedPreferencesManager() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.APP_SETTINGS, Context.MODE_PRIVATE);
    }

    public static String getAndroidId(Context context) {
        return getSharedPreferences(context).getString(Constants.ANDROID_ID, null);
    }

    public static void setAndroidId(Context context, String username) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.ANDROID_ID, username);
        editor.commit();
    }


    public static String getUsername(Context context) {
        return getSharedPreferences(context).getString(Constants.USERNAME, null);
    }

    public static void setUsername(Context context, String username) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.USERNAME, username);
        editor.commit();
    }


    public static String getEmail(Context context) {
        return getSharedPreferences(context).getString(Constants.EMAIL, null);
    }

    public static void setEmail(Context context, String email) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.EMAIL, email);
        editor.commit();
    }


    public static String getMobile(Context context) {
        return getSharedPreferences(context).getString(Constants.PHONE, null);
    }

    public static void setMobile(Context context, String phone) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.PHONE, phone);
        editor.commit();
    }


    public static String getUserID(Context context) {
        return getSharedPreferences(context).getString(Constants.USER_ID, null);
    }

    public static void setUserID(Context context, String userId) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Constants.USER_ID, userId);
        editor.commit();
    }

    public static String getPassword(Context context) {
        return getSharedPreferences(context).getString("Password", null);
    }

    public static void setPassword(Context context, String userId) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Password", userId);
        editor.commit();
    }


    public static String getDeviceToken(Context context) {
        return getSharedPreferences(context).getString("DeviceToken", null);
    }

    public static void setDeviceToken(Context context, String friendID) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("DeviceToken", friendID);
        editor.commit();
    }


    public static String getProfileImage(Context context) {
        return getSharedPreferences(context).getString("ProfileImage", null);
    }

    public static void setProfileImage(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ProfileImage", profileImage);
        editor.commit();
    }

    public static String getType(Context context) {
        return getSharedPreferences(context).getString("Type", null);
    }

    public static void setType(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("Type", profileImage);
        editor.commit();
    }

    public static String getFirmName(Context context) {
        return getSharedPreferences(context).getString("FirmName", null);
    }

    public static void setFirmName(Context context, String profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("FirmName", profileImage);
        editor.commit();
    }

    public static int getCount(Context context) {
        return getSharedPreferences(context).getInt("Count", 0);
    }

    public static void setCount(Context context, int profileImage) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt("Count", profileImage);
        editor.commit();
    }

}
