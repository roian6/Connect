package com.example.connect;

public class SendModel {

    //명함 정보를 담을 양식 클래스

    //명함 정보 저장에 필요한 변수 선언
    private String userkey, to, date;

    public SendModel() {
    }

    public SendModel(String userkey, String to, String date) { //클래스 양식 지정
        this.userkey = userkey;
        this.to = to;
        this.date = date;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
