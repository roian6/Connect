package com.example.connect;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBLoadActivity extends AppCompatActivity {

    //현재 로그인한 유저의 정보를 불러와 UserDB에 저장하는 Activity

    //Firebase Authentication, Database 가져오기
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbload); //DB 로딩 화면 실행

        firebaseAuth = FirebaseAuth.getInstance(); //Firebase 현재 Auth 정보 가져오기
        final FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

        databaseReference.child("user").addChildEventListener(new ChildEventListener() { //Firebase Database의 user 항목에 이벤트 리스너 생성
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { //user 항목에 값이 추가되었다면

                UserModel model = dataSnapshot.getValue(UserModel.class); //추가된 값을 UserModel 양식으로 DB에서 가져옴

                UserDB userDB = new UserDB(); //현재 유저 정보가 담긴 DB 가져오기
                if(model.getUserkey().equals(user.getUid())){ //만약 Firebase DB에서 가져온 값과 유저 정보가 같다면(가져온 유저가 본인이라면)
                    userDB.add(getApplicationContext(), model); //유저 정보 DB에 본인 정보 입력
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish(); //MainActivity로 이동 후 종료
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}
