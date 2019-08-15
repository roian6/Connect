package com.example.connect;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AuthActivity extends AppCompatActivity {

    TextView cateLogin, cateRegi;
    boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //StatusBar 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auth);

        switchFragment(0);

        cateLogin = findViewById(R.id.cate_login);
        cateRegi = findViewById(R.id.cate_regi);

        TransitionDrawable background = (TransitionDrawable) cateLogin.getBackground();
        background.startTransition(200);
        cateLogin.setTextColor(Color.WHITE);
        cateLogin.setTag("y");

        cateLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cateLogin.getTag().equals("n")){
                    TransitionDrawable background = (TransitionDrawable) view.getBackground();
                    background.startTransition(200);
                    cateLogin.setTextColor(Color.WHITE);
                    cateLogin.setTag("y");
                    background = (TransitionDrawable) cateRegi.getBackground();
                    background.reverseTransition(1);
                    cateRegi.setTextColor(getColor(R.color.colorPrimary));
                    cateRegi.setTag("n");

                    switchFragment(0);
                }
            }
        });

        cateRegi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cateRegi.getTag().equals("n")){
                    TransitionDrawable background = (TransitionDrawable) view.getBackground();
                    background.startTransition(200);
                    cateRegi.setTextColor(Color.WHITE);
                    cateRegi.setTag("y");
                    background = (TransitionDrawable) cateLogin.getBackground();
                    background.reverseTransition(1);
                    cateLogin.setTextColor(getColor(R.color.colorPrimary));
                    cateLogin.setTag("n");

                    switchFragment(1);
                }
            }
        });

    }

    void switchFragment(int num){
        Fragment fragment;

        if(num==0) fragment = new LoginFragment();
        else fragment = new RegisterFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(!firstLoad){
            if(num==0) fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            else fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        else firstLoad=false;
        fragmentTransaction.replace(R.id.auth_frame, fragment);
        fragmentTransaction.commit();
    }
}
