package com.example.connect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment3 extends Fragment {

    public static MainFragment3 newInstance(){
        MainFragment3 fragment = new MainFragment3();
        return fragment;
    }


    private FirebaseAuth firebaseAuth;

    RelativeLayout share, logout, password, delete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main3, container, false);

        share = v.findViewById(R.id.main3_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                shareIntent.putExtra(Intent.EXTRA_TEXT, "내 손 안의 전자명함, 커넥트\n" +
                        "https://github.com/roian6/Connect");
                shareIntent.setType("text/plain");
                //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(shareIntent, "앱 공유하기"));
            }
        });

        logout = v.findViewById(R.id.main3_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //Firebase의 현재 Auth 항목을 가져와 로그아웃 실행
                startActivity(new Intent(getContext(), AuthActivity.class)); //로그인 액티비티 실행
                getActivity().finish(); //현재 액티비티 종료
            }
        });

        password = v.findViewById(R.id.main3_password);
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //Dialog Builder 선언
                builder.setTitle("비밀전호 재설정") //Dialog 타이틀 설정
                        .setMessage("이메일 주소로 비밀번호 재설정 메일을 전송합니다") //Dialog 내용 설정
                        .setPositiveButton("전송", new DialogInterface.OnClickListener() { //Dialog에 Positive 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기
                                firebaseAuth.sendPasswordResetEmail(user.getEmail()); //비밀번호 재설정 메일 전송
                                Toast.makeText(getContext(), "성공적으로 전송했습니다!", Toast.LENGTH_SHORT).show(); //전송 완료 토스트
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() { //Dialog에 Negative 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create(); //Builder 반환
                builder.show();
            }
        });

        delete = v.findViewById(R.id.main3_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //Dialog Builder 선언
                builder.setTitle("회원 탈퇴") //Dialog 타이틀 설정
                        .setMessage("정말로 탈퇴하시겠습니까?") //Dialog 내용 설정
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() { //Dialog에 Positive 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기
                                user.delete(); //유저 탈퇴하기
                                Toast.makeText(getContext(), "탈퇴하였습니다.", Toast.LENGTH_SHORT).show(); //탈퇴 확인 토스트
                                getActivity().finishAffinity(); //앱 종료
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() { //Dialog에 Negative 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create(); //Builder 반환
                builder.show();
            }
        });
        return v;
    }
}
