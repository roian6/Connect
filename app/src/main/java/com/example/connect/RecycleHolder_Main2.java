package com.example.connect;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecycleHolder_Main2 extends RecyclerView.ViewHolder {

    //People 탭의 RecyclerView ViewHolder

    //TextView, ImageView 선언
    public TextView name, company, job, date;
    public CircleImageView profile;
    public RelativeLayout box;


    //ViewHolder
    public RecycleHolder_Main2(View itemView) {
        super(itemView);
        //각 아이템들을 RecyclerView 아이템 뷰의 항목과 연결
        name = itemView.findViewById(R.id.item_main2_name);
        company = itemView.findViewById(R.id.item_main2_company);
        job = itemView.findViewById(R.id.item_main2_job);
        profile = itemView.findViewById(R.id.item_main2_profile);
        date = itemView.findViewById(R.id.item_main2_date);
        box = itemView.findViewById(R.id.item_main2_box);

    }

}
