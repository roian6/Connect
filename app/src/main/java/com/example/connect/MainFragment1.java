package com.example.connect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.NFC.SenderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance(){
        MainFragment1 fragment = new MainFragment1();
        return fragment;
    }

    TextView name, company, job, phone, email;
    LinearLayout editBtn, snsBtn, nfcBtn;
    RelativeLayout box;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main1, container, false);

        getElements(v);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.child("card").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                CardModel model = dataSnapshot.getValue(CardModel.class);
                if(model.getUserkey().equals(firebaseAuth.getUid())){
                    name.setText(model.getName());
                    company.setText(model.getCompany());
                    job.setText(model.getJob());
                    phone.setText(model.getPhone());
                    email.setText(model.getEmail());
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

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });

        snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDB userDB = new UserDB();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //Dialog Builder 선언
                builder.setTitle("공유 방법 선택") //Dialog 타이틀 설정
                        .setMessage("명함을 공유할 방법을 선택하세요") //Dialog 내용 설정
                        .setPositiveButton("이미지로 전송", new DialogInterface.OnClickListener() { //Dialog에 Positive 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editBtn.setVisibility(View.INVISIBLE);
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
                                Uri bmpUri = FileProvider.getUriForFile(getContext(), "com.example.connect.fileprovider", file);
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                shareIntent.setType("image/png");
                                //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                editBtn.setVisibility(View.VISIBLE);


                                startActivity(Intent.createChooser(shareIntent, "이미지로 공유하기"));
                            }
                        })
                        .setNegativeButton("텍스트로 전송", new DialogInterface.OnClickListener() { //Dialog에 Negative 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                shareIntent.putExtra(Intent.EXTRA_TEXT, "내 손 안의 전자명함, 커넥트에서 전송된 명함입니다.\n" +
                                        "https://github.com/roian6/Connect\n\n아래의 명함 코드를 앱에서 입력하세요.\n\n"+userDB.getUserKey(getContext()));
                                shareIntent.setType("text/plain");
                                //shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                startActivity(Intent.createChooser(shareIntent, "텍스트로 공유하기"));
                            }
                        });
                builder.create(); //Builder 반환
                builder.show();
            }
        });

        nfcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SenderActivity.class));
            }
        });


        return v;
    }

    void getElements(View v){
        name = v.findViewById(R.id.main1_name);
        company = v.findViewById(R.id.main1_company);
        job = v.findViewById(R.id.main1_job);
        phone = v.findViewById(R.id.main1_phone);
        email = v.findViewById(R.id.main1_email);

        editBtn = v.findViewById(R.id.main1_edit);
        snsBtn = v.findViewById(R.id.main1_sns);
        nfcBtn = v.findViewById(R.id.main1_nfc);

        box = v.findViewById(R.id.main1_box);
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
