package com.example.connect.NFC;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.CardModel;
import com.example.connect.GetTimeDate;
import com.example.connect.R;
import com.example.connect.SendModel;
import com.example.connect.UserDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ReceiverActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    TextView name, company, job, phone, email;
    LinearLayout btn;

    private NfcAdapter nfcAdapter;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        Toolbar toolbar = findViewById(R.id.receive_toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("받은 명함");
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

        getElements();
    }

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter);
        receiveMessageFromDevice(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());

            databaseReference.child("card").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    CardModel model = dataSnapshot.getValue(CardModel.class);
                    if (model.getUserkey().equals(inMessage)) {
                        name.setText(model.getName());
                        company.setText(model.getCompany());
                        job.setText(model.getJob());
                        phone.setText(model.getPhone());
                        email.setText(model.getEmail());

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                GetTimeDate getTimeDate = new GetTimeDate();
                                UserDB userDB = new UserDB();

                                SendModel model = new SendModel();
                                model.setUserkey(inMessage);
                                model.setDate(getTimeDate.getDate());
                                model.setTo(userDB.getUserEmail(getApplicationContext()));

                                databaseReference.child("send").push().setValue(model);

                                finish();
                            }
                        });
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


    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters


        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    void getElements(){
        name = findViewById(R.id.receive_name);
        company = findViewById(R.id.receive_company);
        job = findViewById(R.id.receive_job);
        phone = findViewById(R.id.receive_phone);
        email = findViewById(R.id.receive_email);

        btn = findViewById(R.id.receive_btn);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
