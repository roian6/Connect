package com.example.connect;

public class UserModel {

    //유저 정보를 담을 양식 클래스

    //유저 정보 저장에 필요한 변수 선언
    private String name, email, userkey, profile;

    public UserModel() {
    }

    public UserModel(String name, String email, String userkey, String profile) { //클래스 양식 지정
        this.name = name;
        this.email = email;
        this.userkey = userkey;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

}
