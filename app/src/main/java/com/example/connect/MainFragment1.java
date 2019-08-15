package com.example.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.connect.NFC.SenderActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance(){
        MainFragment1 fragment = new MainFragment1();
        return fragment;
    }

    TextView name, company, job, phone, email;
    LinearLayout editBtn, snsBtn, nfcBtn;

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
    }
}
