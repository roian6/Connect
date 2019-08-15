package com.example.connect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter_Main2 extends RecyclerView.Adapter<RecycleHolder_Main2> {

    //Main2 탭의 RecyclerView Adapter

    List<SendModel> items = new ArrayList<>(); //RecyclerView에 들어갈 아이템 저장 ArrayList 선언

    public List<SendModel> getItems() {
        return items;
    } //List의 아이템을 반환하는 함수

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth firebaseAuth;

    Context context;


    public RecycleAdapter_Main2(Context context){
        this.context = context;
    }

    private String nowDate = "";

    public void add(SendModel data) { //리스트에 값을 추가하는 함수
        items.add(data); //리스트에 양식으로 전달받은 값 추가
        notifyDataSetChanged(); //RecyclerView 갱신
    }

    @Override
    public int getItemViewType(int position) {

        if (items.get(position).getDate().equals(nowDate)) {
            //Log.d("test", items.get(position).getDate()+" / "+nowDate+ "return0");
            return 0;
        } else {
            //Log.d("test", items.get(position).getDate()+" / "+nowDate+ "return1");
            nowDate = items.get(position).getDate();
            return 1;
        }
    }

    @NonNull
    @Override
    public RecycleHolder_Main2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item_main2, parent, false);
            return new RecycleHolder_Main2(v);
        } else {
            //nowDate=changeDate;
            View v1 = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_item_main2_date, parent, false);
            return new RecycleHolder_Main2(v1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleHolder_Main2 holder, final int position) {

        Log.d("test", "wqeq");
        final SendModel item = items.get(position);
        databaseReference.child("card").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final CardModel model = dataSnapshot.getValue(CardModel.class);

                if (model.getUserkey().equals(item.getUserkey())) {
                    holder.name.setText(model.getName());
                    holder.company.setText(model.getCompany());
                    holder.job.setText(model.getJob());
                    holder.date.setText(item.getDate());

                    String data = model.getProfile();
                    byte[] bytePlainOrg = Base64.decode(data, Base64.NO_WRAP);

                    ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                    Bitmap bm = BitmapFactory.decodeStream(inStream);

                    holder.profile.setImageBitmap(bm);

                    holder.box.setVisibility(View.VISIBLE);
                    holder.box.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(context, CardActivity.class);

                            intent.putExtra("card_name", model.getName());
                            intent.putExtra("card_company", model.getCompany());
                            intent.putExtra("card_job", model.getJob());
                            intent.putExtra("card_phone", model.getPhone());
                            intent.putExtra("card_email", model.getEmail());
                            intent.putExtra("card_userkey", model.getUserkey());

                            context.startActivity(intent);
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

    @Override
    public int getItemCount() {
        return items.size(); //리스트 크기 반환
    }


}
