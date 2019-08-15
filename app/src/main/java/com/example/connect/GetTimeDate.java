package com.example.connect;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeDate {

    //현재 시간과 날짜를 형식에 맞게 반환하는 함수

    public String getTime(){ //현재 시간을 반환하는 함수
        Date date = new Date();
        //현재 시간을 (시:분 오전/오후) 형식으로 가져옴
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        String timeNow = simpleDateFormat.format(date);
        return timeNow; //현재 시간 반환
    }

    public String getDate(){ //현재 날짜를 반환하는 함수
        Date date = new Date();
        //현재 날짜를 (년/월/일) 형식으로 가져옴
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateNow = simpleDateFormat.format(date);
        return dateNow; //현재 날짜 반환
    }

}
