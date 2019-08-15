package com.example.connect.NFC;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.R;
import com.example.connect.UserDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        Toolbar toolbar = findViewById(R.id.nfc_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("명함 교환하기");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (!isNfcSupported()) {
            Toast.makeText(this, "NFC가 지원되지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC가 꺼져 있습니다. 설정에서 NFC 기능을 활성화 해주세요.", Toast.LENGTH_SHORT).show();
        }

        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    @Override
    public String getOutcomingMessage() {
        UserDB userDB = new UserDB();
        return userDB.getUserKey(getApplicationContext());
    }

    @Override
    public void signalResult() {

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
