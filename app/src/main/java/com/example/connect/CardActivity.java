package com.example.connect;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class CardActivity extends AppCompatActivity {

    TextView name, company, job, phone, email, companyTxt, jobTxt, phoneTxt, emailTxt;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
