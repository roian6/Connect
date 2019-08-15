package com.example.connect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class CardActivity extends AppCompatActivity {

    TextView name, company, job, phone, email, companyTxt, jobTxt, phoneTxt, emailTxt;
    LinearLayout shareBtn;
    RelativeLayout box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Toolbar toolbar = findViewById(R.id.card_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("명함 정보");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        getElements();

        Intent getIntent = getIntent();
        name.setText(getIntent.getStringExtra("card_name"));
        company.setText(getIntent.getStringExtra("card_company"));
        job.setText(getIntent.getStringExtra("card_job"));
        phone.setText(getIntent.getStringExtra("card_phone"));
        email.setText(getIntent.getStringExtra("card_email"));

        companyTxt.setText(getIntent.getStringExtra("card_company"));
        jobTxt.setText(getIntent.getStringExtra("card_job"));
        phoneTxt.setText(getIntent.getStringExtra("card_phone"));
        emailTxt.setText(getIntent.getStringExtra("card_email"));

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CardActivity.this); //Dialog Builder 선언
                builder.setTitle("공유 방법 선택") //Dialog 타이틀 설정
                        .setMessage("명함을 공유할 방법을 선택하세요") //Dialog 내용 설정
                        .setPositiveButton("이미지로 전송", new DialogInterface.OnClickListener() { //Dialog에 Positive 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                                        + (new Date()).getTime() + ".png";
                                OutputStream out = null;
                                File file = new File(path);
                                try {
                                    out = new FileOutputStream(file);
                                    getBitmapFromView(box).compress(Bitmap.CompressFormat.PNG, 100, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //path = file.getPath();
                                Uri bmpUri = FileProvider.getUriForFile(CardActivity.this, "com.example.connect.fileprovider", file);
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.setType("image/png");
                                //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


                                startActivity(Intent.createChooser(shareIntent, "이미지로 공유하기"));
                            }
                        })
                        .setNegativeButton("텍스트로 전송", new DialogInterface.OnClickListener() { //Dialog에 Negative 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                shareIntent.putExtra(Intent.EXTRA_TEXT, "내 손 안의 전자명함, 커넥트에서 전송된 명함입니다.\n" +
                                        "https://github.com/roian6/Connect\n\n아래의 명함 코드를 앱에서 입력하세요.\n\n"+getIntent.getStringExtra("card_userkey"));
                                shareIntent.setType("text/plain");
                                //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                startActivity(Intent.createChooser(shareIntent, "텍스트로 공유하기"));
                            }
                        });
                builder.create(); //Builder 반환
                builder.show();
            }
        });

    }

    void getElements(){
        name = findViewById(R.id.card_name);
        company = findViewById(R.id.card_company);
        job = findViewById(R.id.card_job);
        phone= findViewById(R.id.card_phone);
        email = findViewById(R.id.card_email);
        companyTxt = findViewById(R.id.card_company_txt);
        jobTxt = findViewById(R.id.card_job_txt);
        phoneTxt = findViewById(R.id.card_phone_txt);
        emailTxt = findViewById(R.id.card_email_txt);

        shareBtn = findViewById(R.id.card_btn);

        box = findViewById(R.id.card_box);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
}
