package com.example.connect;

public class CardModel {

    //명함 정보를 담을 양식 클래스

    //명함 정보 저장에 필요한 변수 선언
    private String name, email, userkey, profile, phone, company, job;

    public CardModel() {
    }

    public CardModel(String name, String email, String userkey, String profile, String phone, String company, String job) { //클래스 양식 지정
        this.name = name;
        this.email = email;
        this.userkey = userkey;
        this.profile = profile;
        this.phone = phone;
        this.company = company;
        this.job = job;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }
}
