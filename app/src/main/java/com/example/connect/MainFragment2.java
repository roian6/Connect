package com.example.connect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainFragment2 extends Fragment {

    public static MainFragment2 newInstance(){
        MainFragment2 fragment = new MainFragment2();
        return fragment;
    }

    Context context;

    RecyclerView rcv;
    RecycleAdapter_Main2 rcvAdap;

    LinearLayout uploadBtn;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main2, container, false);

        context = container.getContext();

        rcv = v.findViewById(R.id.main2_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);

        lm.setReverseLayout(true);
        lm.setStackFromEnd(true);

        rcv.setLayoutManager(lm); //RecyclerView에 LayoutManager 지정

        rcvAdap = new RecycleAdapter_Main2(getContext());

        rcv.setAdapter(rcvAdap);

        final UserDB userDB = new UserDB();
        GetTimeDate getTimeDate = new GetTimeDate();

//        SendModel model = new SendModel();
//        model.setUserkey("oQJCUzuNQOcCwwSa1IN4sZCKTRQ2");
//        model.setTo(userDB.getUserEmail(getContext()));
//        model.setDate("2019/08/14");
//
//        rcvAdap.add(model);
//
//        SendModel model2 = new SendModel();
//        model2.setUserkey("3WodinwpHXVEZ5po1g1KnerFDEB3");
//        model2.setTo(userDB.getUserEmail(getContext()));
//        model2.setDate("2019/08/14");
//
//        rcvAdap.add(model2);
//
//        SendModel model3 = new SendModel();
//        model3.setUserkey("3WodinwpHXVEZ5po1g1KnerFDEB3");
//        model3.setTo(userDB.getUserEmail(getContext()));
//        model3.setDate(getTimeDate.getDate());
//
//        rcvAdap.add(model3);
//
//        SendModel model4 = new SendModel();
//        model4.setUserkey("3WodinwpHXVEZ5po1g1KnerFDEB3");
//        model4.setTo(userDB.getUserEmail(getContext()));
//        model4.setDate(getTimeDate.getDate());
//
//        rcvAdap.add(model4);


//        SendModel model2 = new SendModel();
//        model2.setName("사용자");
//        model2.setCompany("Amazon Web Service");
//        model2.setJob("Server Engineer");
//        model2.setDate("2019/8/15");
//        //model.setDate(getTimeDate.getDate());
//        model2.setProfile(userDB.getProfile(getContext()));
//
//        rcvAdap.add(model2);
//
//        SendModel model3 = new SendModel();
//        model3.setName("유저");
//        model3.setCompany("SI");
//        model3.setJob("Burning");
//        model3.setDate("2019/8/16");
//        //model.setDate(getTimeDate.getDate());
//        model3.setProfile(userDB.getProfile(getContext()));
//
//        rcvAdap.add(model3);

        databaseReference.child("send").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SendModel model = dataSnapshot.getValue(SendModel.class);
                if(model.getTo().equals(userDB.getUserEmail(context))) {
                    Log.d("test", "asd");
                    rcvAdap.add(model);
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

        uploadBtn = v.findViewById(R.id.main2_upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, UploadActivity.class));
            }
        });

        return v;
    }
}
