package com.example.connect;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {


    EditText company, job, phone, email;
    TextView t1, t2, t3, t4;

    Button edtBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("명함 수정");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getElements();

        EditText.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                switch (view.getId()){
                    case R.id.edit_company:
                        t1.setTextColor(b?getColor(R.color.colorPrimary):Color.BLACK);
                        break;
                    case R.id.edit_job:
                        t2.setTextColor(b?getColor(R.color.colorPrimary):Color.BLACK);
                        break;
                    case R.id.edit_phone:
                        t3.setTextColor(b?getColor(R.color.colorPrimary):Color.BLACK);
                        break;
                    case R.id.edit_email:
                        t4.setTextColor(b?getColor(R.color.colorPrimary):Color.BLACK);
                        break;
                }
            }
        };

        company.setOnFocusChangeListener(onFocusChangeListener);
        job.setOnFocusChangeListener(onFocusChangeListener);
        phone.setOnFocusChangeListener(onFocusChangeListener);
        email.setOnFocusChangeListener(onFocusChangeListener);

        edtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserDB userDB = new UserDB();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query query = ref.child("card").orderByChild("userkey").equalTo(userDB.getUserKey(getApplicationContext()));

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                CardModel model = new CardModel(userDB.getUserName(getApplicationContext()), email.getText().toString(),
                        userDB.getUserKey(getApplicationContext()), userDB.getProfile(getApplicationContext()),
                        phone.getText().toString(), company.getText().toString(), job.getText().toString());

                databaseReference.child("card").push().setValue(model);

                finish();

            }
        });
    }

    void getElements(){
        company = findViewById(R.id.edit_company);
        job = findViewById(R.id.edit_job);
        phone = findViewById(R.id.edit_phone);
        email = findViewById(R.id.edit_email);

        t1 = findViewById(R.id.edit_txt1);
        t2 = findViewById(R.id.edit_txt2);
        t3 = findViewById(R.id.edit_txt3);
        t4 = findViewById(R.id.edit_txt4);

        edtBtn = findViewById(R.id.edit_btn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
