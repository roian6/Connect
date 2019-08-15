package com.example.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainFragment3 extends Fragment {

    public static MainFragment3 newInstance(){
        MainFragment3 fragment = new MainFragment3();
        return fragment;
    }


    Button logoutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main3, container, false);

        logoutBtn = v.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //Firebase의 현재 Auth 항목을 가져와 로그아웃 실행
                startActivity(new Intent(getContext(), AuthActivity.class)); //로그인 액티비티 실행
                getActivity().finish(); //현재 액티비티 종료
            }
        });
        return v;
    }
}
