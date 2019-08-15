package com.example.connect;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    LinearLayout upload;
    EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Toolbar toolbar = findViewById(R.id.card_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("명함 등록하기");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        code = findViewById(R.id.upload_code);
        upload = findViewById(R.id.upload_btn);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetTimeDate getTimeDate = new GetTimeDate();
                UserDB userDB = new UserDB();

                SendModel model = new SendModel();
                model.setUserkey(code.getText().toString());
                model.setDate(getTimeDate.getDate());
                model.setTo(userDB.getUserEmail(getApplicationContext()));

                databaseReference.child("send").push().setValue(model);

                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
