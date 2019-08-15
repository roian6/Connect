package com.example.connect;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class UserDB {

    //현재 유저에 대한 정보를 저장하는 DB

    static SharedPreferences getSharedPreferences(Context context) { //SharedPreferences 호출 함수
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void add(Context context, UserModel data) { //DB에 값을 추가하는 함수
        SharedPreferences.Editor editor = getSharedPreferences(context).edit(); //SharedPreference 에디터 생성
        //에디터에 받아온 값 저장
        editor.putString("name", data.getName())
                .putString("email", data.getEmail())
                .putString("userkey", data.getUserkey())
                .putString("profile", data.getProfile())
                .apply(); //SharedPreference에 반영
    }

    //유저 정보 반환 함수
    public String getUserName(Context context) {
        return getSharedPreferences(context).getString("name", "");
    }

    public String getUserEmail(Context context) {
        return getSharedPreferences(context).getString("email", "");
    }

    public String getUserKey(Context context) {
        return getSharedPreferences(context).getString("userkey", "");
    }

    public String getProfile(Context context) {
        return getSharedPreferences(context).getString("profile", "");
    }
}
