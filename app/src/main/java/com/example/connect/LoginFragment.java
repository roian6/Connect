package com.example.connect;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    //EditText, ProgressBar, Button 선언
    EditText id, password;
    Button loginBtn;
    //TextView regiBtn;

    //Firebase Authentication 가져오기
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_login, container, false);

        firebaseAuth = FirebaseAuth.getInstance(); //Firebase 현재 Auth 정보 가져오기

        password = v.findViewById(R.id.login_password);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        //자동 로그인 구현
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) { //Auth 상태 변경 감지
                FirebaseUser user = firebaseAuth.getCurrentUser(); //현재 유저 정보 가져오기

                if (user != null) { //만약 유저 상태가 null이 아니라면(로그인이 되어있는 상태)
                    //Toast.makeText(getContext(), "안녕하세요, " + user.getEmail() + "!", Toast.LENGTH_SHORT).show(); //로그인 성공 토스트
                    startActivity(new Intent(getContext(), DBLoadActivity.class));
                    getActivity().finish(); //DBLoadActivity로 이동 후 종료

                } else { //로그인이 되어 있지 않은 상태
                    //Toast.makeText(getContext(), "로그인을 해주세요", Toast.LENGTH_SHORT).show(); //로그인 요청 토스트
                }

            }
        };

        //Button v.findViewById
        loginBtn = v.findViewById(R.id.btn_login);
        //regiBtn = v.findViewById(R.id.btn_newaccount);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Login 버튼이 클릭되었을 때

                //EditText v.findViewById
                id = v.findViewById(R.id.login_id);
                if (!id.getText().toString().equals("") && !password.getText().toString().equals("")) { //만약 비워진 항목이 없다면
                    login(id.getText().toString(), password.getText().toString()); //입력한 ID와 PW로 로그인 요청
                } else {
                    Toast.makeText(getContext(), "빈칸을 채워주세요", Toast.LENGTH_SHORT).show(); //빈칸 기입 요청 토스트
                }
            }
        });

//        regiBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) { //Register 버튼이 클릭되었을 때
//                startActivity(new Intent(getContext(), RegisterActivity.class)); //RegisterActivity 실행
//            }
//        });

        return v;
    }

    @Override
    public void onStart() { //LoginActivity가 시작할 때
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener); //Auth 상태 변경에 리스너 생성
    }

    @Override
    public void onStop() { //LoginActivity가 끝날 때
        super.onStop();
        if (authStateListener != null) { //만약 Auth 상태 변경 리스너가 남아있는 상태라면
            firebaseAuth.removeAuthStateListener(authStateListener); //리스너 제거
        }
    }

    private void login(String email, String password) { //로그인 함수

//        loginProgress = findViewById(R.id.progress_login);//ProgressBar findViewById
//        loginProgress.setVisibility(View.VISIBLE); //ProgressBar 표시
        firebaseAuth.signInWithEmailAndPassword(email, password) //FireBase에 로그인 요청
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() { //작업 완료 리스너
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //로그인에 성공했다면
                            //Toast.makeText(getContext(), "안녕하세요, "+id.getText().toString()+"!", Toast.LENGTH_SHORT).show(); //로그인 성공 토스트
                            startActivity(new Intent(getContext(), DBLoadActivity.class));
                            getActivity().finish(); //DBLoadActivity 실행 후 종료
                        }
                        else {
                            Toast.makeText(getContext(), "로그인 실패", Toast.LENGTH_SHORT).show(); //로그인 실패 토스트
                            //loginProgress.setVisibility(View.INVISIBLE); //ProgressBar 숨기기
                        }
                    }
                });
    }

    public static class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new AsteriskPasswordTransformationMethod.PasswordCharSequence(source);
        }

        static class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source;
            }
            public char charAt(int index) {
                return '*';
            }
            public int length() {
                return mSource.length();
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end);
            }
        }
    }
}
